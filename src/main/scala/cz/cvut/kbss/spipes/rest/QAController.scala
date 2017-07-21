package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.spipes.exception.SpipesException
import cz.cvut.kbss.spipes.service.QAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

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
      case Right(response) => new ResponseEntity(response, HttpStatus.OK)
      case Left(e: SpipesException) => new ResponseEntity(e.toString(), e.status)
      case Left(e) => new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @GetMapping(path = Array("/{id}/form"), produces = Array("application/json"))
  def gf(@PathVariable id: String): ResponseEntity[Any] =
    service.generateForm(id) match {
      case Right(response) => new ResponseEntity(response, HttpStatus.OK)
      case Left(e: SpipesException) => new ResponseEntity(e.toString(), e.status)
      case Left(e) => new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}