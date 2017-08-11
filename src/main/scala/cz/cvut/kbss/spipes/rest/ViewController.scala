package cz.cvut.kbss.spipes.rest


import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.view.View
import cz.cvut.kbss.spipes.service.ViewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/views"))
class ViewController {

  @Autowired
  private var viewService: ViewService = _

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getAllViews: ResponseEntity[Any] =
    viewService.getAllViews match {
      case Success(views) => new ResponseEntity(views.toSet.asJava, HttpStatus.OK)
      case _ => new ResponseEntity("No views found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllNodes: ResponseEntity[Any] =
    viewService.getAllNodes match {
      case Success(nodes) => new ResponseEntity(nodes, HttpStatus.OK)
      case _ => new ResponseEntity("No nodes found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllEdges: ResponseEntity[Any] =
    viewService.getAllEdges match {
      case Success(edges) => new ResponseEntity(edges, HttpStatus.OK)
      case _ => new ResponseEntity("No edges found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getView(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getView(id) match {
      case Success(Some(v)) => new ResponseEntity(v, HttpStatus.OK)
      case Success(None) => new ResponseEntity("View with id " + id + " not found", HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @GetMapping(path = Array("/{id}/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdges(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getViewEdges(id) match {
      case Success(Some(edges)) => new ResponseEntity(edges.toSet.asJava, HttpStatus.OK)
      case Success(None) => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @GetMapping(path = Array("/{id}/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNodes(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getViewNodes(id) match {
      case Success(Some(nodes)) => new ResponseEntity(nodes.toSet.asJava, HttpStatus.OK)
      case Success(None) => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @GetMapping(path = Array("/edges/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdge(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getEdge(id) match {
      case Success(Some(edge)) => new ResponseEntity(edge, HttpStatus.OK)
      case Success(None) => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @GetMapping(path = Array("/nodes/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNode(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getNode(id) match {
      case Success(Some(node)) => new ResponseEntity(node, HttpStatus.OK)
      case Success(None) => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
      case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @PostMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def saveView(@RequestBody v: View): ResponseEntity[Any] =
    viewService.addView(v) match {
      case Success(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case _ => new ResponseEntity("Not found", HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @PutMapping(path = Array("/{id}"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def updateView(@PathVariable id: String, @RequestBody v: View): ResponseEntity[Any] =
    viewService.updateView(id, v) match {
      case Success(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case _ => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @DeleteMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def deleteView(@PathVariable id: String): ResponseEntity[Any] =
    viewService.deleteView(id) match {
      case Success(u) => new ResponseEntity(u, HttpStatus.NO_CONTENT)
      case _ => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/new"), produces = Array("application/json"))
  def createFromSpipes: ResponseEntity[Any] = {
    viewService.createViewFromSpipes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data") match {
      case Success(u) => new ResponseEntity(u, HttpStatus.CREATED)
      case _ => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{script}/json"), produces = Array("application/json"))
  def loadView(@PathVariable script: String): ResponseEntity[Any] = {
    val kg = viewService.createJsonFromSpipes(script)
    kg match {
      case Success(g) => new ResponseEntity(g, HttpStatus.OK)
      case _ => new ResponseEntity[Any]("KGraph can not be generated", HttpStatus.BAD_REQUEST)
    }
  }
}