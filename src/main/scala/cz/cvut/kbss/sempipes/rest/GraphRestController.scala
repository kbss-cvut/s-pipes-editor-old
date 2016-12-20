package cz.cvut.kbss.sempipes.rest

import java.net.URI

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.{EdgeDao, GraphDao, NodeDao}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/graphs/"))
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
  def getAllGraphs: ResponseEntity[java.util.List[Graph]] =
    graphDao.getAll() match {
      case Some(graphs) =>
        new ResponseEntity(graphs.toBuffer.asJava, HttpStatus.OK)
      case None =>
        new ResponseEntity(Seq[Graph]().asJava, HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/edges/{uri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def loadEdge(@PathVariable uri: String): ResponseEntity[Edge] = {
    val edge = edgeDao.get(new URI("https://edges/" + uri))
    edge match {
      case Some(e) =>
        new ResponseEntity(e, HttpStatus.OK)
      case None =>
        new ResponseEntity(new Edge(), HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{uri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def loadGraph(@PathVariable uri: String): ResponseEntity[Graph] = {
    val graph = graphDao.get(new URI("https://graphs/" + uri))
    graph match {
      case Some(g) =>
        new ResponseEntity(g, HttpStatus.OK)
      case None =>
        new ResponseEntity(new Graph(), HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{uri}/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def loadGraphNodes(@PathVariable uri: String): ResponseEntity[java.util.Set[Node]] =
    graphDao.get(new URI("https://graphs/" + uri)) match {
      case Some(g) =>
        new ResponseEntity(g.getNodes(), HttpStatus.OK)
      case None =>
        new ResponseEntity(Set[Node]().asJava, HttpStatus.NOT_FOUND)
    }
}
