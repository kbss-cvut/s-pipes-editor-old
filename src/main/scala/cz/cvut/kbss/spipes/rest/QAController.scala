package cz.cvut.kbss.spipes.rest

import java.io.FileNotFoundException
import java.util.UUID

import com.fasterxml.jackson.databind.ObjectMapper
import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.rest.QAController.FormDTO
import cz.cvut.kbss.spipes.service.QAService
import cz.cvut.kbss.spipes.util.ConfigParam.DEFAULT_CONTEXT
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.{Logger, PropertySource}
import cz.cvut.sforms.model.Question
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@RestController
@RequestMapping(path = Array("/scripts"))
class QAController extends PropertySource with Logger[QAController] {

  @Autowired
  private var service: QAService = _

  @Autowired
  private var om: ObjectMapper = _

  @PostMapping(
    path = Array("/{script}/forms"),
    consumes = Array("application/json"),
    produces = Array(JsonLd.MEDIA_TYPE))
  def generateForm(
                    @PathVariable script: String,
                    @RequestBody requestDTO: FormDTO,
                  ): ResponseEntity[Any] = {
    log.info("Generating form for script " + script + ", module " + requestDTO.module + " of type " + requestDTO.moduleType)
    service.generateForm(
      script,
      Option(requestDTO.module).getOrElse(getProperty(DEFAULT_CONTEXT) + UUID.randomUUID().toString()),
      requestDTO.moduleType
    ) match {
      case Success(form) =>
        log.info("Form generated successfully for script " + script + ", module " + requestDTO.module + " of type " + requestDTO.moduleType)
        log.trace(form)
        new ResponseEntity(form, HttpStatus.OK)
      case Failure(_: FileNotFoundException) =>
        log.info("Script " + script + " not found")
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  @PostMapping(
    path = Array("/{script}/forms/answers"),
    consumes = Array("application/json"))
  def mergeForm(
                 @PathVariable script: String,
                 @RequestBody answerDto: FormDTO,
               ): ResponseEntity[Any] = {
    answerDto.rootQuestion match {
      case Some(q) =>
        log.info("Received answers for script " + script + ", module " + answerDto.module + ", module type " + answerDto.moduleType)
        log.trace("Root question:" + (q + ""))
        service.mergeForm(script, q, answerDto.moduleType) match {
          case Success(_) => new ResponseEntity(HttpStatus.OK)
          case Failure(e) =>
            log.error(e.getLocalizedMessage(), e)
            new ResponseEntity(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
      case None =>
        log.warn("No answers received for script " + script + ", module " + answerDto.module + ", module type " + answerDto.moduleType)
        new ResponseEntity("Root question must not be empty", HttpStatus.BAD_REQUEST)

    }
  }
}

object QAController {

  case class FormDTO(
                      @BeanProperty module: String,
                      @BeanProperty moduleType: String,
                      @BeanProperty rootQuestion: Option[Question]
                    ) {
    def this() {
      this(null, null, null)
    }
  }

}