package cz.cvut.kbss.spipes.rest

import java.io.FileNotFoundException
import java.util.UUID

import com.fasterxml.jackson.databind.ObjectMapper
import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.spipes.QuestionDTO
import cz.cvut.kbss.spipes.service.QAService
import cz.cvut.kbss.spipes.util.ConfigParam.DEFAULT_CONTEXT
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.{Logger, PropertySource}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

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
                    @RequestBody requestDTO: QuestionDTO,
                  ): ResponseEntity[Any] = {
    log.info("Generating form for script " + script + ", module " + requestDTO.getModule() + " of type " + requestDTO.getModule())
    service.generateForm(
      script,
      Option(requestDTO.getModule()).getOrElse(getProperty(DEFAULT_CONTEXT) + UUID.randomUUID().toString()),
      requestDTO.getModuleType()
    ) match {
      case Success(form) =>
        log.info("Form generated successfully for script " + script + ", module " + requestDTO.getModule() + " of type " + requestDTO.getModuleType())
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
                 @RequestBody answerDto: QuestionDTO,
               ): ResponseEntity[Any] = {
    val module = answerDto.getModule()
    val moduleType = answerDto.getModuleType()
    val rootQuestion = answerDto.getRootQuestion()
    Option(rootQuestion) match {
      case Some(q) =>
        log.info("Received answers for script " + script + ", module " + module + ", module type " + moduleType)
        log.trace("Root question:" + (q + ""))
        service.mergeForm(script, q, moduleType) match {
          case Success(_) => new ResponseEntity(HttpStatus.OK)
          case Failure(e) =>
            log.error(e.getLocalizedMessage(), e)
            new ResponseEntity(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
      case None =>
        log.warn("No answers received for script " + script + ", module " + module + ", module type " + moduleType)
        new ResponseEntity("Root question must not be empty", HttpStatus.BAD_REQUEST)
    }
  }
}