package cz.cvut.kbss.sempipes.controller

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.dao.GraphDao
import cz.cvut.kbss.sempipes.model.graph.{Edge, Node}
import dto.GraphDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
class GraphRestController {

  @BeanProperty
  @Autowired
  var graphDao: GraphDao = _

  @BeanProperty
  @Autowired
  var graphDto: GraphDto = _


  @PostMapping(path = Array("/nodes"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def createNodes(@RequestBody nodes: Seq[Node]) =
    new ResponseEntity(graphDto persistNodes nodes, HttpStatus.CREATED)

  @DeleteMapping(path = Array("/nodes"))
  def deleteNodes(@RequestBody nodes: Seq[Node]) =
    new ResponseEntity(graphDto deleteNodes nodes, HttpStatus.OK)

  @PostMapping(path = Array("/edges"), consumes = Array(JsonLd.MEDIA_TYPE))
  def createEdges(@RequestBody edges: Seq[Edge]) =
    new ResponseEntity(graphDto persistEdges edges, HttpStatus.CREATED)

  @DeleteMapping(Array("/edges"))
  def deleteEdges(@RequestBody edges: Seq[Edge]) =
    new ResponseEntity(graphDto deleteEdges edges, HttpStatus.BAD_REQUEST)
}
