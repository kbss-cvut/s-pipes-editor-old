package cz.cvut.kbss.spipes.websocket

import java.io.File
import java.nio.file._

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager, ScriptManager}
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
@ServerEndpoint(value = "/notifications", configurator = classOf[SpringConfigurator])
class NotificationController extends InitializingBean with PropertySource with Logger[NotificationController] with ResourceManager with ScriptManager {

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
    NotificationController.subscribers.find(_._2.contains(s)) match {
      case Some((script, ss)) if ss.size == 1 =>
        NotificationController.subscribers.remove(script)
      case Some((script, ss)) =>
        NotificationController.subscribers(script) = ss.filterNot(_ == s)
      case None => ()
    }
  }

  @OnMessage
  def register(script: String, session: Session): Unit = {
    Try {
      log.info("Session " + session + " registered on " + script)
      if (NotificationController.subscribers.keySet.contains(script))
        NotificationController.subscribers(script) = NotificationController.subscribers(script) + session
      else
        NotificationController.subscribers(script) = Set(session)
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }


  override def afterPropertiesSet(): Unit = {
    def find(root: File, acc: Set[File]): Set[File] =
      if (root.isDirectory() && !ignored.contains(root))
        root.listFiles() match {
          case s if s.nonEmpty && s.exists(_.isDirectory()) =>
            s.filter(_.isDirectory()).filterNot(_.isHidden()).map(f => find(f, acc)).foldLeft(Set(root))(_ ++ _)
          case _ =>
            acc + root
        }
      else
        acc

    val service = FileSystems.getDefault().newWatchService()

    discoverLocations.flatMap(file => find(file, Set()))
      .foreach(f => {
        val path = Paths.get(f.getAbsolutePath())
        path.register(service, StandardWatchEventKinds.ENTRY_MODIFY)
        log.info(f"""Watch service registered on directory $path""")
      })
    Future(watchFS(service))
  }

  @tailrec
  private def watchFS(service: WatchService): Try[Unit] = {
    log.info("Watch service is waiting for events")
    cleanly(service.take())(_.reset())(wk => {
      if (wk.isValid) {
        val es = wk.pollEvents()
        es.forEach(e => {
          val fileName = wk.watchable().asInstanceOf[Path]
            .resolve(e.asInstanceOf[WatchEvent[Path]].context())
            .toAbsolutePath().toString()
          log.info("Registered FS event on " + fileName)
          if (NotificationController.subscribers.keySet.contains(fileName))
            NotificationController.notify(fileName, e)
        })
      }
    }) match {
      case Failure(t) =>
        log.error(t.getLocalizedMessage())
        log.error(t.getStackTrace().mkString("\n"))
      case _ =>
        log.info("Watch service event processed")
    }
    watchFS(service)
  }
}

object NotificationController extends Logger[NotificationController] {
  private val subscribers = mutable.ParHashMap[String, Set[Session]]()

  def notify(filePath: String, e: WatchEvent[_]*): Unit = {
    NotificationController.subscribers(filePath).foreach((s) => {
      log.info("Sending FS event to " + s.toString())
      s.getBasicRemote().sendText(e.toString())
    })
  }
}