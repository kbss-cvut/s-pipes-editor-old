package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.model.view.{Edge, View, Node}
import cz.cvut.kbss.sempipes.service.ViewService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class ViewControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var service: ViewService = _

  @Test
  def getAllViewsReturnsEmpty = {
    Mockito.when(service.getAllViews()).thenReturn(None)
    val result = mockMvc.perform(get("/views")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getAllViewsReturnsSomeViews = {
    Mockito.when(service.getAllViews()).thenReturn(Some(Set(new View())))
    val result = mockMvc.perform(get("/views")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[{\"uri\":null,\"id\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}]", message)
  }

  @Test
  def getViewReturnsNone = {
    val id = "someId"
    Mockito.when(service.getView(id)).thenReturn(None)
    val result = mockMvc.perform(get("/views/" + id)).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"id\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getViewReturnsSomeView = {
    val v = new View(null, null, null)
    Mockito.when(service.getView(v.getId())).thenReturn(Some(v))
    val result = mockMvc.perform(get("/views/" + v.getId())).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + v.getUri() + "\",\"id\":\"" + v.getId() + "\",\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getViewsEdgesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getViewEdges(id)).thenReturn(None)
    val result = mockMvc.perform(get("/views/" + id + "/edges")).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getViewEdgesReturnsSomeEdges = {
    val id = "someId"
    Mockito.when(service.getViewEdges(id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/views/" + id + "/edges")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getViewNodesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getViewNodes(id)).thenReturn(None)
    val result = mockMvc.perform(get("/views/" + id + "/nodes")).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getViewsNodesReturnsSomeNodes = {
    val id = "someId"
    Mockito.when(service.getViewNodes(id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/views/" + id + "/nodes")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getEdgeReturnsNone = {
    val id = "someId"
    Mockito.when(service.getEdge(id)).thenReturn(None)
    val result = mockMvc.perform(get("/views/edges/" + id)).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"id\":null,\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  @Test
  def getEdgeReturnsSomeEdge = {
    val e = new Edge(null, null)
    val id = e.getId()
    Mockito.when(service.getEdge(id)).thenReturn(Some(e))
    val result = mockMvc.perform(get("/views/edges/" + id)).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + e.getUri() + "\",\"id\":\"" + id + "\",\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  import scala.collection.JavaConverters.setAsJavaSetConverter

  @Test
  def getNodeReturnsNone = {
    val id = "123-456"
    Mockito.when(service.getNode(id)).thenReturn(None)
    val result = mockMvc.perform(get("/views/nodes/" + id)).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"id\":null,\"label\":null,\"x\":0.0,\"y\":0.0,\"nodeTypes\":null,\"inParameters\":null,\"outParameters\":null}", message)
  }

  @Test
  def getNodeReturnsSomeNode = {
    val types = Set("https://type/1",
      "https://type/2",
      "https://type/3")
    val n = new Node("Label", 1, 2, types.asJava, Set[String]().asJava, Set[String]().asJava)
    val id = n.getId()
    Mockito.when(service.getNode(id)).thenReturn(Some(n))
    val result = mockMvc.perform(get("/views/nodes/" + id)).andExpect(status().isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + n.getUri() + "\",\"id\":\"" + id + "\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/1\",\"https://type/2\",\"https://type/3\"],\"inParameters\":[],\"outParameters\":[]}", message)
  }
}