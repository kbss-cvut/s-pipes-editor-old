package cz.cvut.kbss.sempipes.test.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import cz.cvut.kbss.sempipes.test.persistence.BaseDaoTestRunner
import org.junit.Assert._
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters.setAsJavaSetConverter

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */
class GraphDaoTest extends BaseDaoTestRunner {
  private val nodeCount = 123

  @Autowired
  private var graphDao: GraphDao = _

  @Test
  def getAndAddGraph {
    val types = Set[String](
      "https://type/1",
      "https://type/2",
      "https://type/3")
    val n = new Node("Label", 1, 2, types.asJava, Set[String]().asJava, Set[String]().asJava)
    val e = new Edge(n, n)
    val nodes = Set[Node](n)
    val edges = Set[Edge](e)
    val g = new Graph("Graph", nodes.asJava, edges.asJava)
    assertEquals(Some(g), graphDao.add(g))
    assertEquals(Some(g), graphDao.getGraph(g.getUri()))
    assertEquals(g.getNodes, graphDao.getGraph(g.getUri()).get.getNodes())
    assertEquals(Some(g.getUri()), graphDao.delete(g.getUri()))
    assertEquals(None, graphDao.getGraph(g.getUri()))
  }

  @Test
  def getAllGraphs {
    val types = new java.util.HashSet[String]()
    types.add("https://type/1")
    types.add("https://type/2")
    types.add("https://type/3")
    val n = new Node("Label", 1, 2, types, new java.util.HashSet[String](), new java.util.HashSet[String]())
    val e = new Edge(n, n)
    val nodes = Set[Node](n)
    val edges = Set[Edge](e)
    val g01 = new Graph("Graph", nodes.asJava, edges.asJava)
    val g02 = new Graph("Graph", nodes.asJava, edges.asJava)
    val g1 = graphDao.add(g01).get
    val uri1 = g01.getUri()
    val g2 = graphDao.add(g02).get
    val uri2 = g02.getUri()
    assertEquals(g01, g1)
    assertEquals(g02, g2)
    val size = graphDao.getAllGraphs().get.size
    assertEquals(Some(uri1), graphDao.delete(uri1))
    assertEquals(None, graphDao.getGraph(uri1))
    assertEquals(size - 1, graphDao.getAllGraphs().get.size)
    assertEquals(Some(uri2), graphDao.delete(uri2))
    assertEquals(None, graphDao.getGraph(uri2))
    assertTrue(graphDao.getAllGraphs().isEmpty)
  }

  @Test
  def deleteNonExistentGraph {
    assertEquals(None, graphDao.delete(URI.create("https://graph/that/does/not/exist")))
  }
}
