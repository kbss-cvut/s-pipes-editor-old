package cz.cvut.kbss.spipes.websocket

import java.nio.file.{Paths, StandardWatchEventKinds}
import javax.websocket.server.ServerEndpoint
import javax.websocket.{OnError, OnOpen, Session}

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Controller

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
@Controller
@ServerEndpoint("/websocket")
class WebsocketContoller extends InitializingBean {

  private val path = Paths.get("/home/yan/Audiobooks")
  private val ws = path.getFileSystem().newWatchService()
  path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)

  @OnOpen
  def register(s: Session): Unit = WebsocketContoller.sessions += s

  @OnError
  def doNothing(t: Throwable): Unit = ()

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
  private val sessions = mutable.Set[Session]()
}