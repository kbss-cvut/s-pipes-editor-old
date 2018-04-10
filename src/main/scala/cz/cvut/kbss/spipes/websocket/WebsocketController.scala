package cz.cvut.kbss.spipes.websocket

import java.io.File
import java.nio.file._

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.{Logger, PropertySource}
import javax.websocket._
import javax.websocket.server.ServerEndpoint
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.socket.server.standard.SpringConfigurator

import scala.annotation.tailrec
import scala.collection.parallel.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
@Controller
@ServerEndpoint(value = "/websocket", configurator = classOf[SpringConfigurator])
class WebsocketController extends InitializingBean with PropertySource with Logger[WebsocketController] {

  @Autowired
  private var dao: ScriptDao = _

  @OnError
  def onError(t: Throwable): Unit = t match {
    case e: java.io.IOException if e.getMessage() == "java.io.IOException: Broken pipe" => ()
    case _ => log.warn(t.getLocalizedMessage(), t.getStackTrace().mkString("\n\t"))
  }

  @OnClose
  def onClose(s: Session): Unit = {
    log.info("Session closed: " + s.toString())
    WebsocketController.subscribers.find(_._2.contains(s)) match {
      case Some((script, ss)) if ss.size == 1 =>
        WebsocketController.subscribers.remove(script)
      case Some((script, ss)) =>
        WebsocketController.subscribers(script) = ss.filterNot(_ == s)
      case None => ()
    }
  }

  @OnMessage
  def register(script: String, session: Session): Unit = {
    Try {
      log.info("Session " + session + " registered on " + script)
      if (WebsocketController.subscribers.keySet.contains(script))
        WebsocketController.subscribers(script) = WebsocketController.subscribers(script) + session
      else
        WebsocketController.subscribers(script) = Set(session)
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }


  override def afterPropertiesSet(): Unit = {
    def find(root: File, acc: Set[File]): Set[File] =
      if (root.isDirectory())
        root.listFiles() match {
          case s if s.nonEmpty && s.exists(_.isDirectory()) =>
            s.filter(_.isDirectory()).map(f => find(f, acc)).foldLeft(Set(root))(_ ++ _)
          case _ =>
            acc + root
        }
      else
        acc

    dao.discoverLocations.foreach(file => {
      find(file, Set()).foreach(f => {
        val path = Paths.get(f.getAbsolutePath())
        val service = path.getFileSystem().newWatchService()
        path.register(service, StandardWatchEventKinds.ENTRY_MODIFY)
        Future(watchFS(service))
      })
    })
  }

  @tailrec
  private def watchFS(service: WatchService): Unit = {
    val wk = service.take()
    if (wk.isValid) {
      Try {
        val es = wk.pollEvents()
        es.forEach(e => {
          val fileName = wk.watchable().asInstanceOf[Path]
            .resolve(e.asInstanceOf[WatchEvent[Path]].context())
            .toAbsolutePath().toString()
          log.info("Registered FS event on " + fileName)
          if (WebsocketController.subscribers.keySet.contains(fileName))
            WebsocketController.notify(fileName, e)
        })
      } match {
        case Failure(t) =>
          wk.reset()
          log.error(t.getLocalizedMessage())
          log.error(t.getStackTrace().mkString("\n"))
        case _ =>
          wk.reset()
      }
      watchFS(service)
    }
  }
}

object WebsocketController extends Logger[WebsocketController] {
  private val subscribers = mutable.ParHashMap[String, Set[Session]]()

  def notify(filePath: String, e: WatchEvent[_]*): Unit = {
    WebsocketController.subscribers(filePath).foreach((s) => {
      log.info("Sending FS event to " + s.toString())
      s.getBasicRemote().sendText(e.toString())
    })
  }
}