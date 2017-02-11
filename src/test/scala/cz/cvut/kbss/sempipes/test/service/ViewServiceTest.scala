package cz.cvut.kbss.sempipes.test.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.sempipes.Module
import cz.cvut.kbss.sempipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.sempipes.persistence.dao.{SempipesDao, ViewDao}
import cz.cvut.kbss.sempipes.service.{SempipesService, ViewService}
import org.junit.Assert.assertEquals
import org.junit.{Ignore, Test}
import org.mockito.{Matchers, Mockito}
import org.springframework.beans.factory.annotation.Autowired
import org.mockito.Matchers.{any, eq}

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class ViewServiceTest extends BaseServiceTestRunner {

  @Autowired
  private var dao: ViewDao = _

  @Autowired
  private var spipesDao: SempipesDao = _

  @Autowired
  private var service: ViewService = _

  @Autowired
  private var spipesService: SempipesService = _

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

  @Test
  def addViewFails = {
    val v = new View(null, null, null)
    Mockito.when(dao.add(v)).thenReturn(None)
    assertEquals(None, service.addView(v))
  }

  @Test
  def updateViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(dao.update(v.getUri(), v)).thenReturn(Some(v))
    assertEquals(Some(v), service.updateView(v.getId(), v))
  }

  @Test
  def updateViewFails = {
    val v = new View(null, null, null)
    Mockito.when(dao.update(v.getUri(), v)).thenReturn(None)
    assertEquals(None, service.updateView(v.getId(), v))
  }

  @Test
  def deleteViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(dao.delete(v.getUri())).thenReturn(Some(v.getUri()))
    assertEquals(Some(v.getUri()), service.deleteView(v.getId()))
  }

  @Test
  def deleteViewFails = {
    val v = new View(null, null, null)
    Mockito.when(dao.delete(v.getUri())).thenReturn(None)
    assertEquals(None, service.deleteView(v.getId()))
  }

  @Test
  def getViewNodesReturnsNone = {
    val id = "id"
    Mockito.when(service.getViewNodes(id)).thenReturn(None)
    assertEquals(None, service.getViewNodes(id))
  }

  @Test
  def getViewNodesReturnsEmpty = {
    val id = "id"
    Mockito.when(service.getViewNodes(id)).thenReturn(Some(Set[Node]()))
    assertEquals(Some(Set[Node]()), service.getViewNodes(id))
  }

  @Test
  def getViewNodesReturnsNodes = {
    val id = "id"
    Mockito.when(service.getViewNodes(id)).thenReturn(Some(Set(new Node(), new Node())))
    assertEquals(Some(Set(new Node(), new Node())), service.getViewNodes(id))
  }

  @Test
  def getViewEdgesReturnsNone = {
    val id = "id"
    Mockito.when(service.getViewEdges(id)).thenReturn(None)
    assertEquals(None, service.getViewEdges(id))
  }

  @Test
  def getViewEdgesReturnsEmpty = {
    val id = "id"
    Mockito.when(service.getViewEdges(id)).thenReturn(Some(Set[Edge]()))
    assertEquals(Some(Set[Edge]()), service.getViewEdges(id))
  }

  @Test
  def getViewEdgesReturnsEdges = {
    val id = "id"
    val edges = Set(new Edge(), new Edge())
    Mockito.when(service.getViewEdges(id)).thenReturn(Some(edges))
    assertEquals(Some(edges), service.getViewEdges(id))
  }

  @Test
  def getEdgeReturnsNone = {
    val id = "id"
    Mockito.when(service.getEdge(id)).thenReturn(None)
    assertEquals(None, service.getEdge(id))
  }

  @Test
  def getEdgeReturnsEdge = {
    val edge = new Edge()
    Mockito.when(service.getEdge(edge.getId())).thenReturn(Some(edge))
    assertEquals(Some(edge), service.getEdge(edge.getId()))
  }

  @Test
  def getNodeReturnsNone = {
    val id = "id"
    Mockito.when(service.getNode(id)).thenReturn(None)
    assertEquals(None, service.getNode(id))
  }

  @Test
  def getNodeReturnsNode = {
    val node = new Node()
    Mockito.when(service.getNode(node.getId())).thenReturn(Some(node))
    assertEquals(Some(node), service.getNode(node.getId()))
  }

  @Test
  def createViewWhenSpipesServiceReturnsNone = {
    val id = "id"
    Mockito.when(spipesService.getModules(id)).thenReturn(None)
    assertEquals(None, service.createViewFromSempipes(id))
  }

  //todo Finish the stupid test and make more
  @Test
  @Ignore
  def createViewWhenSpipesServiceReturnsEmptyViewIsNotFoundSaveFails = {
    val v = new View()
    val id = v.getId()
    Mockito.when(spipesService.getModules(id)).thenReturn(Some(Set[Module]()))
    Mockito.when(dao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(None)
    Mockito.when(dao.update(any(classOf[URI]), any(classOf[View]))).thenReturn(None)
    assertEquals(None, service.createViewFromSempipes(id))
  }
}
