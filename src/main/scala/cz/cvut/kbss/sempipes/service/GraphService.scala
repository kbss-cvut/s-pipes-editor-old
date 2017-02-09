package cz.cvut.kbss.sempipes.service

import java.net.URI
import java.util.UUID

import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class GraphService {

  @Autowired
  private var dao: GraphDao = _

  @Autowired
  private var sempipesService: SempipesService = _

  def getGraphByUri(uri: String): Option[Graph] =
    dao.get(URI.create(uri))

  def getAllGraphs(): Option[Traversable[Graph]] =
    dao.getAll()

  def addGraph(g: Graph): Option[Graph] =
    dao.add(g)

  def updateGraph(uri: String, g: Graph): Option[Graph] =
    dao.update(URI.create(uri), g)

  def delete(uri: String): Option[URI] =
    dao.delete(URI.create(uri))

  def getGraphNodes(uri: String): Option[Traversable[Node]] =
    dao.getNodes(URI.create(uri))

  def getGraphEdges(uri: String): Option[Traversable[Edge]] =
    dao.getEdges(URI.create(uri))

  def getGraphEdge(graphUri: String, uri: String): Option[Edge] =
    getGraphEdges(graphUri) match {
      case Some(edges) => edges.find(_.getUri == URI.create(uri))
      case _ => None
    }

  def getGraphNode(graphUri: String, uri: String): Option[Node] =
    getGraphNodes(graphUri) match {
      case Some(nodes) => nodes.find(_.getUri == URI.create(uri))
      case _ => None
    }

  def getGraphFromSempipes(uri: String): Option[Graph] = {
    val graphUri = "https://graph.org/g/r/a/p/h/" + UUID.randomUUID().toString()
    sempipesService.getModules(uri) match {
      case Some(modules) =>
        val nodes = modules.map(m =>
          new Node(
            URI.create(graphUri + "nodes/" + UUID.randomUUID()),
            m.getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava))
        val edges = modules.map(m =>
          new Edge(
            URI.create(graphUri + "edges/" + UUID.randomUUID()),
            new Node(URI.create(graphUri + "nodes/" + UUID.randomUUID()),
              m.getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava),
            new Node(URI.create(graphUri + "nodes/" + UUID.randomUUID()),
              m.getNext().getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava)))
          .filterNot(e => e.getDestinationNode == null)
        dao.get(URI.create(uri)) match {
          case Some(graph) =>
            graph.setNodes(nodes.toSet.asJava)
            graph.setEdges(edges.toSet.asJava)
            Some(dao.update(graph.getUri, graph).get)
          case None =>
            val graph = new Graph(URI.create(graphUri),
              "Some mr. graph man",
              nodes.toSet.asJava,
              edges.toSet.asJava)
            Some(dao.add(graph).get)
        }
      case None =>
        None
    }
  }
}