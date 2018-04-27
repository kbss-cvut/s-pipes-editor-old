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
@ServerEndpoint(value = "/executions/request", configurator = classOf[SpringConfigurator])
class ExecutionRequestController extends Logger[ExecutionRequestController] {

  @Autowired
  private var dao: ScriptDao = _

  @OnMessage
  def onExecutionRequest(msg: String, session: Session): Unit = {
    Try {
      log.error(s"Request :$msg")
      executions += UUID.randomUUID() -> session
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }
}
