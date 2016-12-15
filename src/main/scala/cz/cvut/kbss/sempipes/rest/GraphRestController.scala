package cz.cvut.kbss.sempipes.rest

import java.net.URI

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.persistence.dao.{EdgeDao, GraphDao, NodeDao}
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import org.apache.http.HttpResponse
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
  def login = new ResponseEntity(new Node(new URI("https://uri"), "L", 1, 1, Set("type").asJava, Set("in").asJava, Set("out").asJava), HttpStatus.OK)

  @GetMapping(path = Array("/admin/"))
  def admin = new ResponseEntity("Permission granted to admin", HttpStatus.OK)

  @GetMapping(path = Array("/graphs/{relativeUri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def loadGraph(@PathVariable relativeUri: String): ResponseEntity[Any] =
    graphDao.get(new URI("https://graphs/" + relativeUri)) match {
      case Some(g) =>
        new ResponseEntity(g, HttpStatus.OK)
      case None =>
        new ResponseEntity[Any](None, HttpStatus.NOT_FOUND)
    }
}
