package cz.cvut.kbss.spipes.rest

import java.io.FileNotFoundException

import cz.cvut.kbss.spipes.service.QAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@RestController
@RequestMapping(path = Array("/nodes"))
class QAController {

  @Autowired
  private var service: QAService = _

  @PostMapping(path = Array("/{id}/form"), produces = Array("application/json"))
  def generateForm(@PathVariable id: String): ResponseEntity[Any] =
    service.generateForm(id) match {
      case Success(form) => new ResponseEntity(form, HttpStatus.OK)
      case Failure(e: FileNotFoundException) => new ResponseEntity(HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}