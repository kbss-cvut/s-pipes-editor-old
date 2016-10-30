package spring.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._
import spring.model.dao.NodeDao
import spring.model.graph.Edge

import scala.beans.BeanProperty
import scala.util.parsing.json.JSON

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
class EdgeRestController {

  @Autowired
  @BeanProperty
  var nodeDao: NodeDao = _

  @PostMapping(Array("/"))
  def createEdge(@RequestBody ids: (Long, Long)) = {
    val from = nodeDao getNodeById ids._1
    val to = nodeDao getNodeById ids._2
    val edge = Edge(from, to)
    new ResponseEntity(edge, HttpStatus.OK)
  }

  @DeleteMapping(Array("/"))
  def deleteEdge(@RequestBody json: String) = {
    JSON parseFull json match {
      case Some(parsed) =>
        val map = parsed.asInstanceOf[Map[String, Double]]
        val from = map("from") toLong
        val to = map("to") toLong

        println(from)
        println(to)

        new ResponseEntity("Edge deleted", HttpStatus.OK)
      case None =>
        new ResponseEntity("Request syntax error", HttpStatus.BAD_REQUEST)
    }
  }
}
