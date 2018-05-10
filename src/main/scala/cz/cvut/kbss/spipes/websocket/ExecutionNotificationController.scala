package cz.cvut.kbss.spipes.websocket

import cz.cvut.kbss.spipes.service.ExecutionService
import cz.cvut.kbss.spipes.util.Logger
import javax.websocket._
import javax.websocket.server.ServerEndpoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.socket.server.standard.SpringConfigurator

import scala.collection.parallel.mutable
import scala.collection.parallel.mutable.ParHashMap
import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.07.2017.
  */
/** Unused **/
@Controller
@ServerEndpoint(value = "/executions", configurator = classOf[SpringConfigurator])
class ExecutionNotificationController extends Logger[ExecutionNotificationController] {

  @Autowired
  private var service: ExecutionService = _

  /*@OnMessage
  def onExecutionRequest(msg: String, session: Session): Unit = {
    Try {
      log.error(s"Request :$msg")
      val executionId = UUID.randomUUID().toString()
      executions += executionId -> session
      service.requestExecution("", executionId)
    } match {
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
      case _ => ()
    }
  }*/
}

object ExecutionNotificationController {
  val executions: ParHashMap[String, Session] = mutable.ParHashMap[String, Session]()

  def notify(executionId: String): Try[Unit] =
    executions.get(executionId) match {
      case Some(s) => Success(s.getBasicRemote().sendText(executionId))
      case None => Failure(new IllegalArgumentException(s"Execution with id $executionId not found"))
    }
}
