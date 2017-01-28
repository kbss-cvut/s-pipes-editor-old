package cz.cvut.kbss.sempipes.test.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.{Edge, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import cz.cvut.kbss.sempipes.service.GraphService
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.junit.Assert.assertEquals

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class GraphServiceTest extends BaseServiceTestRunner {

  @Autowired
  private var dao: GraphDao = _

  @Autowired
  private var service: GraphService = _

  @Test
  def getNodeWhenDaoReturnsNone = {
    val graphId = "graphId"
    val id = "id"
    Mockito.when(dao.getNodes(URI.create(graphId))).thenReturn(None)
    assertEquals(None, service.getGraphNode(graphId, id))
  }

  @Test
  def getNodeWhenDaoReturnsEmpty = {
    val graphId = "graphId"
    val id = "id"
    Mockito.when(dao.getNodes(URI.create(graphId))).thenReturn(Some(Set()))
    assertEquals(None, service.getGraphNode(graphId, id))
  }

  @Test
  def getNodeWhenDaoReturnsNodes = {
    val graphId = "graphId"
    val id = "id"
    val node1 = new Node(URI.create(id), "label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val node2 = new Node(URI.create(id + 1), "label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    Mockito.when(dao.getNodes(URI.create(graphId))).thenReturn(Some(Set(node1, node2)))
    assertEquals(Some(node1), service.getGraphNode(graphId, id))
  }

  @Test
  def getEdgeWhenDaoReturnsNone = {
    val graphId = "graphId"
    val id = "id"
    Mockito.when(dao.getEdges(URI.create(graphId))).thenReturn(None)
    assertEquals(None, service.getGraphEdge(graphId, id))
  }

  @Test
  def getEdgeWhenDaoReturnsEmpty = {
    val graphId = "graphId"
    val id = "id"
    Mockito.when(dao.getEdges(URI.create(graphId))).thenReturn(Some(Set()))
    assertEquals(None, service.getGraphEdge(graphId, id))
  }

  @Test
  def getEdgeWhenDaoReturnsEdges = {
    val graphId = "graphId"
    val id = "id"
    val node1 = new Node(URI.create(id), "label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val edge1 = new Edge(URI.create(id), node1, node1)
    val edge2 = new Edge(URI.create(id + "1"), node1, node1)
    Mockito.when(dao.getEdges(URI.create(graphId))).thenReturn(Some(Set(edge1, edge2)))
    assertEquals(Some(edge1), service.getGraphEdge(graphId, id))
  }
}
