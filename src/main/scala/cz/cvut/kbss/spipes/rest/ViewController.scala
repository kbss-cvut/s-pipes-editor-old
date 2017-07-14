package cz.cvut.kbss.spipes.rest


import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.view.View
import cz.cvut.kbss.spipes.service.{FileWatcher, ViewService}
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/views"))
class ViewController extends InitializingBean {

  @Autowired
  private var viewService: ViewService = _

  @Autowired
  private var fileWatcher: FileWatcher = _

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getAllViews: ResponseEntity[Any] =
    viewService.getAllViews match {
      case Some(views) => new ResponseEntity(views, HttpStatus.OK)
      case None => new ResponseEntity("No views found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllNodes: ResponseEntity[Any] =
    viewService.getAllNodes match {
      case Some(nodes) => new ResponseEntity(nodes, HttpStatus.OK)
      case None => new ResponseEntity("No nodes found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getAllEdges: ResponseEntity[Any] =
    viewService.getAllEdges match {
      case Some(edges) => new ResponseEntity(edges, HttpStatus.OK)
      case None => new ResponseEntity("No edges found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getView(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getView(id) match {
      case Some(v) => new ResponseEntity(v, HttpStatus.OK)
      case None => new ResponseEntity("View with id " + id + "not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdges(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getViewEdges(id) match {
      case Some(edges) => new ResponseEntity(edges, HttpStatus.OK)
      case None => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNodes(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getViewNodes(id) match {
      case Some(nodes) => new ResponseEntity(nodes, HttpStatus.OK)
      case None => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/edges/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdge(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getEdge(id) match {
      case Some(edge) => new ResponseEntity(edge, HttpStatus.OK)
      case None => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/nodes/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNode(@PathVariable id: String): ResponseEntity[Any] =
    viewService.getNode(id) match {
      case Some(node) => new ResponseEntity(node, HttpStatus.OK)
      case None => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @PostMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def saveView(@RequestBody v: View): ResponseEntity[Any] =
    viewService.addView(v) match {
      case Some(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case None => new ResponseEntity("Not found", HttpStatus.INTERNAL_SERVER_ERROR)
    }

  @PutMapping(path = Array("/{id}"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def updateView(@PathVariable id: String, @RequestBody v: View): ResponseEntity[Any] =
    viewService.updateView(id, v) match {
      case Some(view) => new ResponseEntity(view, HttpStatus.CREATED)
      case None => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @DeleteMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def deleteView(@PathVariable id: String): ResponseEntity[Any] =
    viewService.deleteView(id) match {
      case Some(u) => new ResponseEntity(u, HttpStatus.NO_CONTENT)
      case None => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/new"), produces = Array("application/json"))
  def createFromSpipes: ResponseEntity[Any] = {
    viewService.createViewFromSpipes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data") match {
      case Some(u) => new ResponseEntity(u, HttpStatus.CREATED)
      case None => new ResponseEntity("Not found", HttpStatus.NOT_FOUND)
    }
  }

  override def afterPropertiesSet(): Unit = Future(fileWatcher.watch())
}