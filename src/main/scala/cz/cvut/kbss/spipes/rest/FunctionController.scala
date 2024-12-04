package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.spipes.model.dto.QuestionDTO
import cz.cvut.kbss.spipes.service.FunctionService
import cz.cvut.kbss.spipes.util.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters.asJavaIterableConverter
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.04.2018.
  */

@RestController
@RequestMapping(path = Array("/functions"))
class FunctionController extends Logger[FunctionController] {

  @Autowired
  private var service: FunctionService = _

  @GetMapping
  def listFunctions = new ResponseEntity(service.listFunctions.toSeq.sortBy(_.getScriptPath()).asJava, HttpStatus.OK)

  @PostMapping(path = Array("/execute"))
  def executeFunction(@RequestBody dto: QuestionDTO): ResponseEntity[_] = {
    val q = dto.getRootQuestion()
    service.requestExecution(q) match {
      case Success((id, status)) if status == HttpStatus.OK =>
        log.info(s"Execution with ID $id created")
        new ResponseEntity(id, HttpStatus.CREATED)
      case Success((id, status)) =>
        log.info(s"Execution creation returned status $status for ID $id")
        new ResponseEntity(id, status)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getStackTrace().mkString("\n"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}
