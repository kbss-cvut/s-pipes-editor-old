package cz.cvut.kbss.sempipes.service

import java.net.URI
import java.util.UUID

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class GraphService {

  @Autowired
  private var dao: GraphDao = _

  @Autowired
  private var sempipesService: SempipesService = _

  def getGraphById(id: String): Option[Graph] =
    dao.getGraph(URI.create(Vocabulary.s_c_graph + id))

  def getAllGraphs(): Option[Traversable[Graph]] =
    dao.getAllGraphs()

  def addGraph(g: Graph): Option[Graph] =
    dao.add(g)

  def updateGraph(id: String, g: Graph): Option[Graph] =
    dao.update(URI.create(Vocabulary.s_c_graph + id), g)

  def delete(id: String): Option[URI] =
    dao.delete(URI.create(Vocabulary.s_c_graph + id))

  def getGraphNodes(id: String): Option[Traversable[Node]] =
    dao.getNodes(URI.create(Vocabulary.s_c_graph + id))

  def getGraphEdges(id: String): Option[Traversable[Edge]] =
    dao.getEdges(URI.create(Vocabulary.s_c_graph + id))

  def getEdge(id: String): Option[Edge] =
    dao.getEdge(URI.create(Vocabulary.s_c_edge + id))

  def getNode(id: String): Option[Node] =
    dao.getNode(URI.create(Vocabulary.s_c_node + id))


  import scala.collection.JavaConverters._

  def getGraphFromSempipes(id: String): Option[Graph] = {
    val graphUri = Vocabulary.s_c_graph + UUID.randomUUID().toString()
    sempipesService.getModules(id) match {
      case Some(modules) =>
        val nodes = modules.map(m => new Node(m.getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava))
        val edges = modules.map(m => new Edge(
          new Node(m.getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava),
          new Node(m.getNext().getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava)))
          .filterNot(e => e.getDestinationNode == null)
        dao.getGraph(URI.create(id)) match {
          case Some(graph) =>
            graph.setNodes(nodes.toSet.asJava)
            graph.setEdges(edges.toSet.asJava)
            Some(dao.update(graph.getUri, graph).get)
          case None =>
            val graph = new Graph("Some mr. graph man",
              nodes.toSet.asJava,
              edges.toSet.asJava)
            Some(dao.add(graph).get)
        }
      case None =>
        None
    }
  }
}