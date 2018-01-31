package cz.cvut.kbss.spipes.rest

import java.io.FileNotFoundException

import com.fasterxml.jackson.databind.ObjectMapper
import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.rest.QAController.{FormRequestDTO, _}
import cz.cvut.kbss.spipes.service.QAService
import cz.cvut.kbss.spipes.util.Implicits._
import org.slf4j.LoggerFactory
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
class QAController {

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
                    @RequestBody requestDTO: FormRequestDTO,
                  ): ResponseEntity[Any] = {
    log.info("Generating form for script " + script + ", module " + requestDTO.module + " of type " + requestDTO.moduleType)
    service.generateForm(script, requestDTO.module, requestDTO.moduleType) match {
      case Success(form) =>
        log.info("Form generated successfully for script " + script + ", module " + requestDTO.module + " of type " + requestDTO.moduleType)
        log.trace(form)
        new ResponseEntity(form, HttpStatus.OK)
      case Failure(_: FileNotFoundException) =>
        log.info("Script " + script + " not found")
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}

object QAController {

  private final val log = LoggerFactory.getLogger(classOf[QAController])

  case class FormRequestDTO(
                             @BeanProperty module: String,
                             @BeanProperty moduleType: String
                           ) {
    def this() {
      this(null, null)
    }
  }

}