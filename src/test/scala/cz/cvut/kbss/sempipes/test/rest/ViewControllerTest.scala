package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.service.GraphService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class GraphControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var service: GraphService = _

  @Test
  def getAllGraphsReturnsEmpty = {
    Mockito.when(service.getAllGraphs()).thenReturn(None)
    val result = mockMvc.perform(get("/graphs")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getAllGraphsReturnsSomeGraphs = {
    Mockito.when(service.getAllGraphs()).thenReturn(Some(Set(new Graph())))
    val result = mockMvc.perform(get("/graphs")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[{\"uri\":null,\"id\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}]", message)
  }

  @Test
  def getGraphReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphById(id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id)).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"id\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getGraphReturnsSomeGraph = {
    val g = new Graph(null, null, null)
    Mockito.when(service.getGraphById(g.getId())).thenReturn(Some(g))
    val result = mockMvc.perform(get("/graphs/" + g.getId())).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + g.getUri() + "\",\"id\":\"" + g.getId() + "\",\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getGraphEdgesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphEdges(id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id + "/edges")).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphEdgesReturnsSomeEdges = {
    val id = "someId"
    Mockito.when(service.getGraphEdges(id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/graphs/" + id + "/edges")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphNodesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphNodes(id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id + "/nodes")).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphNodesReturnsSomeNodes = {
    val id = "someId"
    Mockito.when(service.getGraphNodes(id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/graphs/" + id + "/nodes")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getEdgeReturnsNone = {
    val id = "someId"
    Mockito.when(service.getEdge(id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/edges/" + id)).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"id\":null,\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  @Test
  def getEdgeReturnsSomeEdge = {
    val e = new Edge(null, null)
    val id = e.getId()
    Mockito.when(service.getEdge(id)).thenReturn(Some(e))
    val result = mockMvc.perform(get("/graphs/edges/" + id)).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + e.getUri() + "\",\"id\":\"" + id + "\",\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  import scala.collection.JavaConverters.setAsJavaSetConverter

  @Test
  def getNodeReturnsNone = {
    val id = "123-456"
    Mockito.when(service.getNode(id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/nodes/" + id)).andExpect(status.isNotFound()).andReturn
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
    val result = mockMvc.perform(get("/graphs/nodes/" + id)).andExpect(status().isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + n.getUri() + "\",\"id\":\"" + id + "\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/1\",\"https://type/2\",\"https://type/3\"],\"inParameters\":[],\"outParameters\":[]}", message)
  }
}