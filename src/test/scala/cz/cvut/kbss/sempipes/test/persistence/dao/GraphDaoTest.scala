package cz.cvut.kbss.sempipes.test.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import cz.cvut.kbss.sempipes.test.persistence.BaseDaoTestRunner
import org.junit.Assert._
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */
class GraphDaoTest extends BaseDaoTestRunner {
  private val nodeCount = 123

  @Autowired
  private var graphDao: GraphDao = _

  @Test
  def getAndAddGraph {
    val uri = new URI("https://graphs/11")

    //todo WrapperSet support
    val types = new java.util.HashSet[String]()
    types.add("https://type/1")
    types.add("https://type/2")
    types.add("https://type/3")
    val n = new Node(new URI("https://nodes/11" + nodeCount), "Label", 1, 2, types, new java.util.HashSet[String](), new java.util.HashSet[String]())
    val e = new Edge(new URI("https://edges/11"), n, n)
    val nodes = new java.util.HashSet[Node]()
    nodes.add(n)
    val edges = new java.util.HashSet[Edge]()
    edges.add(e)

    val g = new Graph(uri, "Graph", nodes, edges)
    assertEquals(Some(g), graphDao.add(g))
    assertNotNull(graphDao.get(uri))
    assertEquals(Some(g), graphDao.get(uri))
    assertEquals(g.getNodes, graphDao.get(uri).get.getNodes)
    assertEquals(Some(uri), graphDao.delete(uri))
    assertEquals(None, graphDao.get(uri))
  }

  @Test
  def getAllGraphs {
    val uri1 = new URI("https://graphs/1")
    val uri2 = new URI("https://graphs/2")
    val uri = new URI("https://graphs/11")

    //todo WrapperSet support (cz.cvut.kbss.jopa.sessions.CollectionInstanceBuilder.java: 79 throws OWLPersistenceException)
    val types = new java.util.HashSet[String]()
    types.add("https://type/1")
    types.add("https://type/2")
    types.add("https://type/3")
    val n = new Node(new URI("https://nodes/11" + nodeCount), "Label", 1, 2, types, new java.util.HashSet[String](), new java.util.HashSet[String]())
    val e = new Edge(new URI("https://edges/11"), n, n)
    val nodes = new java.util.HashSet[Node]()
    nodes.add(n)
    val edges = new java.util.HashSet[Edge]()
    edges.add(e)

    val g1 = new Graph(uri1, "Graph", nodes, edges)
    val g2 = new Graph(uri2, "Graph", nodes, edges)
    assertEquals(Some(g1), graphDao.add(g1))
    assertNotNull(graphDao.get(uri1))
    assertEquals(Some(g2), graphDao.add(g2))
    assertNotNull(graphDao.get(uri2))
    val size = graphDao.getAll().get.size
    assertEquals(Some(uri1), graphDao.delete(uri1))
    assertEquals(None, graphDao.get(uri1))
    assertEquals(size - 1, graphDao.getAll().get.size)
    assertEquals(Some(uri2), graphDao.delete(uri2))
    assertEquals(None, graphDao.get(uri2))
    assertTrue(graphDao.getAll().isEmpty)
  }

  @Test
  def deleteNonExistentGraph {
    assertEquals(None, graphDao.delete(URI.create("https://graph/that/does/not/exist")))
  }
}
