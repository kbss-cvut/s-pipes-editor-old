package cz.cvut.kbss.spipes.websocket

import java.nio.file.{Paths, StandardWatchEventKinds}
import javax.websocket.server.ServerEndpoint
import javax.websocket.{OnClose, OnError, OnOpen, Session}

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Controller

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.parallel.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
@Controller
@ServerEndpoint("/websocket")
class WebsocketContoller extends InitializingBean {

  private final val log = LoggerFactory.getLogger(classOf[WebsocketContoller])

  private val path = Paths.get("/home/yan/Audiobooks")
  private val ws = path.getFileSystem().newWatchService()
  path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)

  @OnOpen
  def onOpen(s: Session): Unit = {
    log.info("Session registered: " + s.toString())
    WebsocketContoller.sessions += s
  }

  @OnError
  def onError(t: Throwable): Unit = {
    log.error(t.getLocalizedMessage())
    log.error(t.getStackTrace().mkString("\n"))
  }

  @OnClose
  def close(s: Session): Unit = {
    log.info("Session closed: " + s.toString())
    WebsocketContoller.sessions -= s
  }

  override def afterPropertiesSet(): Unit = {
    Future {
      while (true) { // fixme More reactive? At least not while true
        val wk = ws.take()
        if (wk != null) {
          val es = wk.pollEvents().asScala
          WebsocketContoller.sessions.foreach(s => s.getBasicRemote.sendText(es.mkString("", "\n", "\n")))
          wk.reset()
        }
      }
    }
  }
}

object WebsocketContoller {
  private val sessions = mutable.ParSet[Session]()
}