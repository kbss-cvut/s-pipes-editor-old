package cz.cvut.kbss.spipes.websocket

import java.util.UUID

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.Logger
import javax.websocket._
import javax.websocket.server.ServerEndpoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.socket.server.standard.SpringConfigurator

import scala.util.{Failure, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
@Controller
@ServerEndpoint(value = "/executions/notify", configurator = classOf[SpringConfigurator])
class ExecutionNotificationController extends Logger[ExecutionNotificationController] {

  @Autowired
  private var dao: ScriptDao = _

  @OnMessage
  def onExecutionNotification(msg: String, session: Session): Unit = {
    Try {
      log.error(s"Notification: $msg")
      executions += UUID.randomUUID() -> session
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }
}
