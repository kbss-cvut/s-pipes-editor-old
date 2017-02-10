package cz.cvut.kbss.sempipes.test.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.sempipes.persistence.dao.ViewDao
import cz.cvut.kbss.sempipes.service.ViewService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class ViewServiceTest extends BaseServiceTestRunner {

  @Autowired
  private var dao: ViewDao = _

  @Autowired
  private var service: ViewService = _

  @Test
  def getNodeWhenDaoReturnsNone = {
    val id = "id"
    Mockito.when(dao.getNode(URI.create(Vocabulary.s_c_node + "/" + id))).thenReturn(None)
    assertEquals(None, service.getNode(id))
  }

  @Test
  def getNodeWhenDaoReturnsNode = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    Mockito.when(dao.getNode(node1.getUri)).thenReturn(Some(node1))
    assertEquals(Some(node1), service.getNode(node1.getId))
  }

  @Test
  def getEdgeWhenDaoReturnsNone = {
    val id = "id"
    Mockito.when(dao.getEdge(URI.create(Vocabulary.s_c_edge + "/" + id))).thenReturn(None)
    assertEquals(None, service.getEdge(id))
  }


  @Test
  def getEdgeWhenDaoReturnsEdge = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val edge1 = new Edge(node1, node1)
    Mockito.when(dao.getEdge(edge1.getUri)).thenReturn(Some(edge1))
    assertEquals(Some(edge1), service.getEdge(edge1.getId()))
  }

  @Test
  def getViewReturnsNone = {
    val id = "id"
    Mockito.when(dao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(None)
    assertEquals(None, service.getView(id))
  }

  @Test
  def getViewReturnsView = {
    val v = new View(null, null, null)
    val id = v.getId()
    Mockito.when(dao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(Some(v))
    assertEquals(Some(v), service.getView(id))
  }

  @Test
  def getAllViewsReturnsNone = {
    Mockito.when(dao.getAllViews()).thenReturn(None)
    assertEquals(None, service.getAllViews())
  }

  @Test
  def getAllViewsReturnsEmpty = {
    Mockito.when(dao.getAllViews()).thenReturn(Some(Set[View]()))
    assertEquals(Some(Set()), service.getAllViews())
  }

  @Test
  def getAllViewsReturnsSomeViews = {
    val s = Set(new View(null, null, null), new View(null, null, null), new View(null, null, null))
    Mockito.when(dao.getAllViews()).thenReturn(Some(s))
    assertEquals(Some(s), service.getAllViews())
  }

  @Test
  def addViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(dao.add(v)).thenReturn(Some(v))
    assertEquals(Some(v), service.addView(v))
  }
}
