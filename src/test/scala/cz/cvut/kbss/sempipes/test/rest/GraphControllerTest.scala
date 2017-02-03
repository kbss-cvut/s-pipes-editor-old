package cz.cvut.kbss.sempipes.test.rest

import java.net.URI

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
    val result = mockMvc.perform(get("/graphs")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getAllGraphsReturnsSomeGraphs = {
    Mockito.when(service.getAllGraphs()).thenReturn(Some(Set(new Graph())))
    val result = mockMvc.perform(get("/graphs")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[{\"uri\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}]", message)
  }

  @Test
  def getGraphReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphByUri("https://graphs/" + id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id)).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getGraphReturnsSomeGraph = {
    val id = "someId"
    Mockito.when(service.getGraphByUri("https://graphs/" + id)).thenReturn(Some(new Graph()))
    val result = mockMvc.perform(get("/graphs/" + id)).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getGraphEdgesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphEdges("https://graphs/" + id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id + "/edges")).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphEdgesReturnsSomeEdges = {
    val id = "someId"
    Mockito.when(service.getGraphEdges("https://graphs/" + id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/graphs/" + id + "/edges")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphNodesReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphNodes("https://graphs/" + id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id + "/nodes")).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphNodesReturnsSomeNodes = {
    val id = "someId"
    Mockito.when(service.getGraphNodes("https://graphs/" + id)).thenReturn(Some(Set()))
    val result = mockMvc.perform(get("/graphs/" + id + "/nodes")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getGraphEdgeReturnsNone = {
    val id = "someId"
    Mockito.when(service.getGraphEdge("https://graphs/" + id, id)).thenReturn(None)
    val result = mockMvc.perform(get("/graphs/" + id + "/edges/" + id)).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  @Test
  def getGraphEdgeReturnsSomeEdge = {
    val id = "someId"
    Mockito.when(service.getGraphEdge("https://graphs/" + id, id)).thenReturn(Some(new Edge()))
    val result = mockMvc.perform(get("/graphs/" + id + "/edges/" + id)).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":null,\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  @Test
  def getGraphNodeReturnsSomeNode = {
    val id = "someId"
    val types = new java.util.HashSet[String]
    types.add("https://type/1")
    types.add("https://type/2")
    types.add("https://type/3")
    val n = new Node(URI.create("https://" + id), "Label", 1, 2, types, new java.util.HashSet[String](), new java.util.HashSet[String]())
    Mockito.when(service.getGraphNode("https://graphs/" + id, id)).thenReturn(Some(n))
    val result = mockMvc.perform(get("/graphs/" + id + "/nodes/" + id)).andExpect(status().isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"https://someId\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/1\",\"https://type/2\",\"https://type/3\"],\"inParameters\":[],\"outParameters\":[]}", message)
  }
}