package cz.cvut.kbss.sempipes.rest

import java.net.URI

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.service.GraphService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/graphs"))
class GraphController {

  @Autowired
  private var graphService: GraphService = _

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getAllGraphs: ResponseEntity[java.util.Set[Graph]] =
    graphService.getAllGraphs() match {
      case Some(graphs) => new ResponseEntity(graphs.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Graph]().asJava, HttpStatus.OK)
    }

  @GetMapping(path = Array("/{uri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getGraph(@PathVariable uri: String): ResponseEntity[Graph] = {
    graphService.getGraphByUri("https://graphs/" + uri) match {
      case Some(g) => new ResponseEntity(g, HttpStatus.OK)
      case None => new ResponseEntity(new Graph(), HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{uri}/edges"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdges(@PathVariable uri: String): ResponseEntity[java.util.Set[Edge]] = {
    graphService.getGraphEdges("https://graphs/" + uri) match {
      case Some(edges) => new ResponseEntity(edges.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Edge]().asJava, HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{uri}/nodes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNodes(@PathVariable uri: String): ResponseEntity[java.util.Set[Node]] =
    graphService.getGraphNodes("https://graphs/" + uri) match {
      case Some(nodes) => new ResponseEntity(nodes.toSet.asJava, HttpStatus.OK)
      case None => new ResponseEntity(Set[Node]().asJava, HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{graphUri}/edges/{uri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getEdge(@PathVariable graphUri: String, @PathVariable uri: String): ResponseEntity[Edge] = {
    graphService.getGraphEdge("https://graphs/" + graphUri, uri) match {
      case Some(edge) => new ResponseEntity(edge, HttpStatus.OK)
      case None => new ResponseEntity(new Edge(), HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{graphUri}/nodes/{uri}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getNode(@PathVariable graphUri: String, @PathVariable uri: String): ResponseEntity[Node] = {
    graphService.getGraphNode("https://graphs/" + graphUri, uri) match {
      case Some(node) => new ResponseEntity(node, HttpStatus.OK)
      case None => new ResponseEntity(new Node(), HttpStatus.NOT_FOUND)
    }
  }

  @PostMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def saveGraph(@RequestBody g: Graph): ResponseEntity[Graph] =
    graphService.addGraph(g) match {
      case Some(graph) => new ResponseEntity(graph, HttpStatus.CREATED)
    }

  @PutMapping(path = Array("/{uri}"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def updateGraph(@PathVariable uri: String, @RequestBody g: Graph): ResponseEntity[Graph] =
    graphService.updateGraph(uri, g) match {
      case Some(graph) => new ResponseEntity(graph, HttpStatus.CREATED)
      case None => new ResponseEntity(new Graph(), HttpStatus.NOT_FOUND)
    }

  def deleteMapping(@PathVariable uri: String): ResponseEntity[URI] =
    graphService.delete(uri) match {
      case Some(u) => new ResponseEntity(u, HttpStatus.NO_CONTENT)
      case None => new ResponseEntity(URI.create("https://not/found"), HttpStatus.NOT_FOUND)
    }
}