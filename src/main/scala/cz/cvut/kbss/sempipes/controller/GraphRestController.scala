package cz.cvut.kbss.sempipes.controller

import java.net.URI

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.dao.{EdgeDao, GraphDao, NodeDao}
import cz.cvut.kbss.sempipes.model.graph.{Edge, Node}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

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
  var nodeDao: NodeDao = _

  @BeanProperty
  @Autowired
  var edgeDao: EdgeDao = _

  @GetMapping(path = Array("/"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNode = new ResponseEntity(nodeDao getNode new URI("https://uri"), HttpStatus.OK)

  @PostMapping(path = Array("/nodes"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  @ResponseBody
  def createNodes(@RequestBody nodes: java.util.List[Node]) = {
    new ResponseEntity(nodeDao.addNodes(nodes.asScala), HttpStatus.CREATED)
  }

  @DeleteMapping(path = Array("/nodes"))
  def deleteNodes(@RequestBody nodes: Seq[Node]) =
    new ResponseEntity(nodeDao.deleteNodes(nodes), HttpStatus.OK)

  @PostMapping(path = Array("/edges"), consumes = Array(JsonLd.MEDIA_TYPE))
  def createEdges(@RequestBody edges: Seq[Edge]) =
    new ResponseEntity(edgeDao.addEdges(edges), HttpStatus.CREATED)

  @DeleteMapping(Array("/edges"))
  def deleteEdges(@RequestBody edges: Seq[Edge]) =
    new ResponseEntity(edgeDao.deleteEdges(edges), HttpStatus.BAD_REQUEST)

  @GetMapping(path = Array("/graphs/{uri}"))
  def getGraph(@PathVariable uri: String) =
    new ResponseEntity(graphDao.getGraph(new URI(uri)), HttpStatus.OK)


}
