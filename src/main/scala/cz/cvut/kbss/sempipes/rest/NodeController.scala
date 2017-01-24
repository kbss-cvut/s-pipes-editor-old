package cz.cvut.kbss.sempipes.rest

import cz.cvut.kbss.sempipes.model.graph.Node
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

  @GetMapping(path = Array("/{id}"))
  def getNode(@PathVariable id: String): ResponseEntity[Node] = {
    service.getNodeById(id) match {
      case Some(n) => new ResponseEntity(n, HttpStatus.OK)
      case None => new ResponseEntity(new Node(), HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{id}/form"))
  def generateForm(@PathVariable id: String) = {
    service.generateForm(id)
  }
}