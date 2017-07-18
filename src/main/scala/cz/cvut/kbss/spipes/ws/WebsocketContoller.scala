package cz.cvut.kbss.spipes.ws

import java.nio.file.{Paths, StandardWatchEventKinds}
import javax.websocket.server.ServerEndpoint
import javax.websocket.{OnError, OnMessage, OnOpen, Session}

import org.springframework.stereotype.Controller

import scala.collection.JavaConverters.asScalaBufferConverter

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
@Controller
@ServerEndpoint("/ws")
class WebsocketContoller {

  private val path = Paths.get("/home/yan/Audiobooks")
  private val ws = path.getFileSystem().newWatchService()
  path.register(ws, StandardWatchEventKinds.ENTRY_CREATE)

  @OnOpen
  @OnError
  def doNothing(session: Session): Unit = ()

  @OnMessage
  def handleMessage(message: String, session: Session): Unit = {
    val wk = ws.poll()
    if (wk != null) {
      session.getBasicRemote.sendText(wk.pollEvents().asScala.mkString("", "\n", "\n"))
      wk.reset()
    }
  }
}