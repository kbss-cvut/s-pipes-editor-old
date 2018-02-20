package cz.cvut.kbss.spipes.websocket

import java.nio.file._
import javax.websocket._
import javax.websocket.server.ServerEndpoint

import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits.configParamValue
import cz.cvut.kbss.spipes.{Logger, PropertySource}
import org.springframework.beans.factory.InitializingBean
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
      val fileName = getProperty(SCRIPTS_LOCATION) + "/" + script
      if (WebsocketController.subscribers.keySet.contains(fileName))
        WebsocketController.subscribers(fileName) = WebsocketController.subscribers(fileName) + session
      else
        WebsocketController.subscribers(fileName) = Set(session)
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }


  override def afterPropertiesSet(): Unit = {
    val path = Paths.get(getProperty(SCRIPTS_LOCATION))
    val service = path.getFileSystem().newWatchService()
    path.register(service, StandardWatchEventKinds.ENTRY_MODIFY)

    Future(watchFS(service))
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
            WebsocketController.subscribers(fileName).foreach((s) => {
              log.info("Sending FS event to " + s.toString())
              s.getBasicRemote().sendText(e.toString())
            })
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

object WebsocketController {
  private val subscribers = mutable.ParHashMap[String, Set[Session]]()
}