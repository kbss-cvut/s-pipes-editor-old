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
import scala.util.{Failure, Success}

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
    val t = new Throwable()
    Mockito.when(nodeDao.get(URI.create(Vocabulary.s_c_node + "/" + id))).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getNode(id))
  }

  @Test
  def getNodeWhenDaoReturnsNode = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    Mockito.when(nodeDao.get(node1.getUri)).thenReturn(Success(Some(node1)))
    assertEquals(Success(Some(node1)), service.getNode(node1.getId))
  }

  @Test
  def getEdgeWhenDaoReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(edgeDao.get(URI.create(Vocabulary.s_c_edge + "/" + id))).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getEdge(id))
  }


  @Test
  def getEdgeWhenDaoReturnsEdge = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val edge1 = new Edge(node1, node1)
    Mockito.when(edgeDao.get(edge1.getUri)).thenReturn(Success(Some(edge1)))
    assertEquals(Success(Some(edge1)), service.getEdge(edge1.getId()))
  }

  @Test
  def getViewReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getView(id))
  }

  @Test
  def getViewReturnsView = {
    val v = new View(null, null, null)
    val id = v.getId()
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(Success(Some(v)))
    assertEquals(Success(Some(v)), service.getView(id))
  }

  @Test
  def getAllViewsReturnsNone = {
    val t = new Throwable()
    Mockito.when(viewDao.findAll).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getAllViews)
  }

  @Test
  def getAllViewsReturnsEmpty = {
    Mockito.when(viewDao.findAll).thenReturn(Success(Set[View]()))
    assertEquals(Success(Set()), service.getAllViews)
  }

  @Test
  def getAllViewsReturnsSomeViews = {
    val s = Set(new View(null, null, null), new View(null, null, null), new View(null, null, null))
    Mockito.when(viewDao.findAll).thenReturn(Success(s))
    assertEquals(Success(s), service.getAllViews)
  }

  @Test
  def addViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.save(v)).thenReturn(Success(v))
    assertEquals(Success(v), service.addView(v))
  }

  @Test
  def addViewFails = {
    val v = new View(null, null, null)
    val t = new Throwable()
    Mockito.when(viewDao.save(v)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.addView(v))
  }

  @Test
  def updateViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.updateView(v.getUri(), v)).thenReturn(Success(v))
    assertEquals(Success(v), service.updateView(v.getId(), v))
  }

  @Test
  def updateViewFails = {
    val v = new View(null, null, null)
    val t = new Throwable()
    Mockito.when(viewDao.updateView(v.getUri(), v)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.updateView(v.getId(), v))
  }

  @Test
  def deleteViewSucceeds = {
    val v = new View(null, null, null)
    Mockito.when(viewDao.delete(v.getUri())).thenReturn(Success(v.getUri()))
    assertEquals(Success(v.getUri()), service.deleteView(v.getId()))
  }

  @Test
  def deleteViewFails = {
    val v = new View(null, null, null)
    val t = new Throwable()
    Mockito.when(viewDao.delete(v.getUri())).thenReturn(Failure(t))
    assertEquals(Failure(t), service.deleteView(v.getId()))
  }

  @Test
  def getViewNodesReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(service.getViewNodes(id)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getViewNodes(id))
  }

  @Test
  def getViewNodesReturnsEmpty = {
    val id = "id"
    Mockito.when(service.getViewNodes(id)).thenReturn(Success(Some(Set[Node]())))
    assertEquals(Success(Some(Set[Node]())), service.getViewNodes(id))
  }

  @Test
  def getViewNodesReturnsNodes = {
    val id = "id"
    Mockito.when(service.getViewNodes(id)).thenReturn(Success(Some(Set(new Node(), new Node()))))
    assertEquals(Success(Some(Set(new Node(), new Node()))), service.getViewNodes(id))
  }

  @Test
  def getViewEdgesReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(service.getViewEdges(id)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getViewEdges(id))
  }

  @Test
  def getViewEdgesReturnsEmpty = {
    val id = "id"
    Mockito.when(service.getViewEdges(id)).thenReturn(Success(Some(Set[Edge]())))
    assertEquals(Success(Some(Set[Edge]())), service.getViewEdges(id))
  }

  @Test
  def getViewEdgesReturnsEdges = {
    val id = "id"
    val edges = Set(new Edge(), new Edge())
    Mockito.when(service.getViewEdges(id)).thenReturn(Success(Some(edges)))
    assertEquals(Success(Some(edges)), service.getViewEdges(id))
  }

  @Test
  def getEdgeReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(service.getEdge(id)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getEdge(id))
  }

  @Test
  def getEdgeReturnsEdge = {
    val edge = new Edge()
    Mockito.when(service.getEdge(edge.getId())).thenReturn(Success(Some(edge)))
    assertEquals(Success(Some(edge)), service.getEdge(edge.getId()))
  }

  @Test
  def getNodeReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(service.getNode(id)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.getNode(id))
  }

  @Test
  def getNodeReturnsNode = {
    val node = new Node()
    Mockito.when(service.getNode(node.getId())).thenReturn(Success(Some(node)))
    assertEquals(Success(Some(node)), service.getNode(node.getId()))
  }

  @Test
  def createViewWhenSpipesServiceReturnsNone = {
    val id = "id"
    val t = new Throwable()
    Mockito.when(spipesService.getModules(id)).thenReturn(Failure(t))
    assertEquals(Failure(t), service.createViewFromSpipes(id))
  }

  //todo Finish the stupid test and make more
  @Test
  @Ignore
  def createViewWhenSpipesServiceReturnsEmptyViewIsNotFoundSaveFails = {
    val v = new View()
    val id = v.getId()
    val t = new Throwable()
    Mockito.when(spipesService.getModules(id)).thenReturn(Success(Set[Module]()))
    Mockito.when(viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))).thenReturn(Failure(t))
    Mockito.when(viewDao.updateView(any(classOf[URI]), any(classOf[View]))).thenReturn(Failure(t))
    assertEquals(Failure(t), service.createViewFromSpipes(id))
  }
}
