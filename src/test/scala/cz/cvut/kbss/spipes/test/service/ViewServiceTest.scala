package cz.cvut.kbss.spipes.test.service

import java.net.URI

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.spipes.Module
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.{EdgeDao, NodeDao, SpipesDao, ViewDao}
import cz.cvut.kbss.spipes.service.{SpipesService, ViewService}
import org.junit.Assert.assertEquals
import org.junit.{Ignore, Test}
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class ViewServiceTest extends BaseServiceTestRunner {

  @Autowired
  private var viewDao: ViewDao = _

  @Autowired
  private var nodeDao: NodeDao = _

  @Autowired
  private var edgeDao: EdgeDao = _

  @Autowired
  private var spipesDao: SpipesDao = _

  @Autowired
  private var service: ViewService = _

  @Autowired
  private var spipesService: SpipesService = _

  @Test
  def getNodeWhenDaoReturnsNone = {
    val id = "id"
    Mockito.when(nodeDao.get(URI.create(Vocabulary.s_c_node + "/" + id))).thenReturn(None)
    assertEquals(None, service.getNode(id))
  }

  @Test
  def getNodeWhenDaoReturnsNode = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    Mockito.when(nodeDao.get(node1.getUri)).thenReturn(Some(node1))
    assertEquals(Some(node1), service.getNode(node1.getId))
  }

  @Test
  def getEdgeWhenDaoReturnsNone = {
    val id = "id"
    Mockito.when(edgeDao.get(URI.create(Vocabulary.s_c_edge + "/" + id))).thenReturn(None)
    assertEquals(None, service.getEdge(id))
  }


  @Test
  def getEdgeWhenDaoReturnsEdge = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val edge1 = new Edge(node1, node1)
    Mockito.when(edgeDao.get(edge1.getUri)).thenReturn(Some(edge1))
    assertEquals(Some(edge1), service.getEdge(edge1.getId()))
  }

  @Test
  def getViewReturnsNone = {
    val id = "id"
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(None)
    assertEquals(None, service.getView(id))
  }

  @Test
  def getViewReturnsView = {
    val v = new View(null, null, null)
    val id = v.getId()
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(Some(v))
    assertEquals(Some(v), service.getView(id))
  }

  @Test
  def getAllViewsReturnsNone = {
    Mockito.when(viewDao.getAllViews).thenReturn(None)
    assertEquals(None, service.getAllViews)
  }

  @Test
  def getAllViewsReturnsEmpty = {
    Mockito.when(viewDao.getAllViews).thenReturn(Some(Set[View]()))
    assertEquals(Some(Set()), service.getAllViews)
  }

  @Test
  def getAllViewsReturnsSomeViews = {
    val s = Set(new View(null, null, null), new View(null, null, null), new View(null, null, null))
    Mockito.when(viewDao.getAllViews).thenReturn(Some(s))
    assertEquals(Some(s), service.getAllViews)
  }

  @Test
  def addViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.save(v)).thenReturn(Some(v))
    assertEquals(Some(v), service.addView(v))
  }

  @Test
  def addViewFails = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.save(v)).thenReturn(None)
    assertEquals(None, service.addView(v))
  }

  @Test
  def updateViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.updateView(v.getUri(), v)).thenReturn(Some(v))
    assertEquals(Some(v), service.updateView(v.getId(), v))
  }

  @Test
  def updateViewFails = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.updateView(v.getUri(), v)).thenReturn(None)
    assertEquals(None, service.updateView(v.getId(), v))
  }

  @Test
  def deleteViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.delete(v.getUri())).thenReturn(Some(v.getUri()))
    assertEquals(Some(v.getUri()), service.deleteView(v.getId()))
  }

  @Test
  def deleteViewFails = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.delete(v.getUri())).thenReturn(None)
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
    assertEquals(None, service.createViewFromSpipes(id))
  }

  //todo Finish the stupid test and make more
  @Test
  @Ignore
  def createViewWhenSpipesServiceReturnsEmptyViewIsNotFoundSaveFails = {
    val v = new View()
    val id = v.getId()
    Mockito.when(spipesService.getModules(id)).thenReturn(Some(Set[Module]()))
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(None)
    Mockito.when(viewDao.updateView(any(classOf[URI]), any(classOf[View]))).thenReturn(None)
    assertEquals(None, service.createViewFromSpipes(id))
  }
}
