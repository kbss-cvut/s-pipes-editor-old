package cz.cvut.kbss.sempipes.rest

import cz.cvut.kbss.sempipes.service.QAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@RestController
@RequestMapping(path = Array("/nodes"))
@PreAuthorize("hasRole('ANY_ROLE')")
class QAController {

  @Autowired
  private var service: QAService = _

  @PostMapping(path = Array("/{id}/form"), produces = Array("application/json"))
  def generateForm(@PathVariable id: String): ResponseEntity[Any] =
    service.generateForm(id) match {
      case Some(response) => new ResponseEntity(response, HttpStatus.OK)
      case None => new ResponseEntity("Node with id " + id + " not found", HttpStatus.NOT_FOUND)
    }
}