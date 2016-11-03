package spring.controller

import java.io.{FileInputStream, InputStream}
import java.util

import cz.cvut.kbss.jsonld.JsonLd
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._
import spring.model.dao.NodeDao

import scala.beans.BeanProperty
import scala.util.parsing.json.JSON

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
class GraphRestController {

  @Autowired
  @BeanProperty
  var nodeDao: NodeDao = _

  @PostMapping(path = Array("/nodes"), consumes = Array(JsonLd.MEDIA_TYPE))
  def createNode(@RequestBody json: String) = {

    // TODO: Parse stuff, create module, send response
  }

  @DeleteMapping(path = Array("/nodes"))
  def deleteNode(@RequestBody json: String) = {
    // TODO: Parse stuff, delete module, send response
  }

  @PostMapping(path = Array("/edges"), consumes = Array(JsonLd.MEDIA_TYPE))
  def createEdge(@RequestBody json: String) = {
    JSON parseFull json match {
      case Some(parsed) =>
        val map = parsed.asInstanceOf[Map[String, Double]]
        val from = map("from") toLong
        val to = map("to") toLong

        // TODO: Create module dependency

        println(from)
        println(to)

        new ResponseEntity("EDGE WILL BE HERE!", HttpStatus.CREATED)
      case None =>
        new ResponseEntity("Request syntax error", HttpStatus.BAD_REQUEST)
    }
  }

  @DeleteMapping(Array("/edges"))
  def deleteEdge(@RequestBody json: String) = {
    JSON parseFull json match {
      case Some(parsed) =>
        val map = parsed.asInstanceOf[Map[String, String]]
        val from = map("fromUri")
        val to = map("toUri")

        // TODO: Delete module dependency

        println(from)
        println(to)

        new ResponseEntity("Edge deleted", HttpStatus.NO_CONTENT)
      case None =>
        new ResponseEntity("Request syntax error", HttpStatus.BAD_REQUEST)
    }
  }
}
