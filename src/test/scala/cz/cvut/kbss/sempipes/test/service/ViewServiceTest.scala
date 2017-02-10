package cz.cvut.kbss.sempipes.test.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.view.{Edge, Node}
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
    Mockito.when(dao.getNode(URI.create(Vocabulary.s_c_node + id))).thenReturn(None)
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
    Mockito.when(dao.getEdge(URI.create(Vocabulary.s_c_edge + id))).thenReturn(None)
    assertEquals(None, service.getEdge(id))
  }


  @Test
  def getEdgeWhenDaoReturnsEdge = {
    val node1 = new Node("label", 0, 0, Set[String]().asJava, Set[String]().asJava, Set[String]().asJava)
    val edge1 = new Edge(node1, node1)
    Mockito.when(dao.getEdge(edge1.getUri)).thenReturn(Some(edge1))
    assertEquals(Some(edge1), service.getEdge(edge1.getId))
  }
}
