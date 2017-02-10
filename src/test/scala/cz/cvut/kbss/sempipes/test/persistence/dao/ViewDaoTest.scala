package cz.cvut.kbss.sempipes.test.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.sempipes.persistence.dao.ViewDao
import cz.cvut.kbss.sempipes.test.persistence.BaseDaoTestRunner
import org.junit.Assert._
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters.setAsJavaSetConverter

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */
class ViewDaoTest extends BaseDaoTestRunner {
  private val nodeCount = 123

  @Autowired
  private var dao: ViewDao = _

  @Test
  def getAndAddView {
    val types = Set[String](
      "https://type/1",
      "https://type/2",
      "https://type/3")
    val n = new Node("Label", 1, 2, types.asJava, Set[String]().asJava, Set[String]().asJava)
    val e = new Edge(n, n)
    val nodes = Set[Node](n)
    val edges = Set[Edge](e)
    val v = new View("View", nodes.asJava, edges.asJava)
    assertEquals(Some(v), dao.add(v))
    assertEquals(Some(v), dao.get(v.getUri()))
    assertEquals(v.getNodes, dao.get(v.getUri()).get.getNodes())
    assertEquals(Some(v.getUri()), dao.delete(v.getUri()))
    assertEquals(None, dao.get(v.getUri()))
  }

  @Test
  def getAllViews {
    val types = new java.util.HashSet[String]()
    types.add("https://type/1")
    types.add("https://type/2")
    types.add("https://type/3")
    val n = new Node("Label", 1, 2, types, new java.util.HashSet[String](), new java.util.HashSet[String]())
    val e = new Edge(n, n)
    val nodes = Set[Node](n)
    val edges = Set[Edge](e)
    val v01 = new View("View", nodes.asJava, edges.asJava)
    val v02 = new View("View", nodes.asJava, edges.asJava)
    val v1 = dao.add(v01).get
    val uri1 = v01.getUri()
    val v2 = dao.add(v02).get
    val uri2 = v02.getUri()
    assertEquals(v01, v1)
    assertEquals(v02, v2)
    val size = dao.getAllViews().get.size
    assertEquals(Some(uri1), dao.delete(uri1))
    assertEquals(None, dao.get(uri1))
    assertEquals(size - 1, dao.getAllViews().get.size)
    assertEquals(Some(uri2), dao.delete(uri2))
    assertEquals(None, dao.get(uri2))
    assertTrue(dao.getAllViews().isEmpty)
  }

  @Test
  def deleteNonExistentView {
    assertEquals(None, dao.delete(URI.create("https://view/that/does/not/exist")))
  }
}
