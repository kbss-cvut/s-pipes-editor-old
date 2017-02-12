package cz.cvut.kbss.sempipes.rest

import cz.cvut.kbss.sempipes.model.view.Node
import cz.cvut.kbss.sempipes.service.NodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@RestController
@RequestMapping(path = Array("/nodes"))
class NodeController {

  @Autowired
  private var service: NodeService = _

  @PostMapping(path = Array("/{id}/form"), produces = Array("application/json"))
  def generateForm(@PathVariable id: String): ResponseEntity[Any] =
    service.generateForm(id) match {
      case Some(response) => new ResponseEntity(response, HttpStatus.OK)
      case None => new ResponseEntity("Node with id " + id + " not found", HttpStatus.NOT_FOUND)
    }
}