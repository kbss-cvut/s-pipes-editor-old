package cz.cvut.kbss.spipes.rest


import java.net.URI

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.service.ViewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/views"))
class ViewController {

  @Autowired
  private var viewService: ViewService = _

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getAllViews: ResponseEntity[java.util.Set[View]] =
    viewService.getAllViews() match {
      case Some(views) => new ResponseEntity(views.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[View]().asJava, HttpStatus.OK)
    }

  @GetMapping(path = Array("/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllNodes: ResponseEntity[java.util.Set[Node]] =
    viewService.getAllNodes() match {
      case Some(nodes) => new ResponseEntity(nodes.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Node]().asJava, HttpStatus.OK)
    }

  @GetMapping(path = Array("/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllEdges: ResponseEntity[java.util.Set[Edge]] =
    viewService.getAllEdges() match {
      case Some(edges) => new ResponseEntity(edges.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Edge]().asJava, HttpStatus.OK)
    }

  @GetMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getView(@PathVariable id: String): ResponseEntity[View] =
    viewService.getView(id) match {
      case Some(v) => new ResponseEntity(v, HttpStatus.OK)
      case None => new ResponseEntity(new View(), HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdges(@PathVariable id: String): ResponseEntity[java.util.Set[Edge]] =
    viewService.getViewEdges(id) match {
      case Some(edges) => new ResponseEntity(edges.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Edge]().asJava, HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNodes(@PathVariable id: String): ResponseEntity[java.util.Set[Node]] =
    viewService.getViewNodes(id) match {
      case Some(nodes) => new ResponseEntity(nodes.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Node]().asJava, HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/edges/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdge(@PathVariable id: String): ResponseEntity[Edge] =
    viewService.getEdge(id) match {
      case Some(edge) => new ResponseEntity(edge, HttpStatus.OK)
      case None => new ResponseEntity(new Edge(), HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/nodes/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNode(@PathVariable id: String): ResponseEntity[Node] =
    viewService.getNode(id) match {
      case Some(node) => new ResponseEntity(node, HttpStatus.OK)
      case None => new ResponseEntity(new Node(), HttpStatus.NOT_FOUND)
    }

  @PostMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def saveView(@RequestBody v: View): ResponseEntity[View] =
    viewService.addView(v) match {
      case Some(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case None => new ResponseEntity(new View(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @PutMapping(path = Array("/{id}"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def updateView(@PathVariable id: String, @RequestBody v: View): ResponseEntity[View] =
    viewService.updateView(id, v) match {
      case Some(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case None => new ResponseEntity(new View(), HttpStatus.NOT_FOUND)
    }

  @DeleteMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def deleteView(@PathVariable id: String): ResponseEntity[URI] =
    viewService.deleteView(id) match {
      case Some(u) => new ResponseEntity(u, HttpStatus.NO_CONTENT)
      case None => new ResponseEntity(URI.create("https://not/found"), HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/new"), produces = Array(JsonLd.MEDIA_TYPE))
  def createFromSpipes =
    viewService.createViewFromspipes("http://kbss.felk.cvut.cz/spipes-sped/contexts/12/data") match {
      case Some(u) => new ResponseEntity(u, HttpStatus.CREATED)
      case None => new ResponseEntity(URI.create("https://not/found"), HttpStatus.NOT_FOUND)
    }
}