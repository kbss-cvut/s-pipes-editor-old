package cz.cvut.kbss.spipes.test.rest

import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.service.ViewService
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
class ViewControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var service: ViewService = _

  @Test
  def getAllViewsReturnsEmpty = {
    Mockito.when(service.getAllViews).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views")).andExpect(status.isNotFound()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("No views found", message)
  }

  @Test
  def getAllViewsReturnsSomeViews = {
    Mockito.when(service.getAllViews).thenReturn(Success(Set(new View())))
    val result = mockMvc.perform(get("/views")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[{\"uri\":null,\"id\":null,\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}]", message)
  }

  @Test
  def getViewFails = {
    val id = "someId"
    Mockito.when(service.getView(id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views/" + id)).andExpect(status.isInternalServerError()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getViewReturnsSomeView = {
    val v = new View(null, null, null)
    Mockito.when(service.getView(v.getId())).thenReturn(Success(Some(v)))
    val result = mockMvc.perform(get("/views/" + v.getId())).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + v.getUri() + "\",\"id\":\"" + v.getId() + "\",\"label\":null,\"nodes\":null,\"edges\":null,\"contentHash\":null,\"author\":null}", message)
  }

  @Test
  def getViewsEdgesFails = {
    val id = "someId"
    Mockito.when(service.getViewEdges(id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views/" + id + "/edges")).andExpect(status.isInternalServerError()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getViewEdgesReturnsEmpty = {
    val id = "someId"
    Mockito.when(service.getViewEdges(id)).thenReturn(Success(Some(Set())))
    val result = mockMvc.perform(get("/views/" + id + "/edges")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getViewEdgesReturnsSomeEdges = {
    val n = new Node()
    val e1 = new Edge(n, null)
    val e2 = new Edge(null, null)
    val id = "id"
    Mockito.when(service.getViewEdges(id)).thenReturn(Success(Some(Set(e1, e2))))
    val result = mockMvc.perform(get("/views/" + id + "/edges")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertTrue(
      message == "[{\"uri\":\"" + e1.getUri() + "\",\"id\":\"" + e1.getId() + "\",\"sourceNode\":{\"uri\":null,\"id\":null,\"label\":null,\"x\":0.0,\"y\":0.0,\"nodeTypes\":null,\"inParameters\":null,\"outParameters\":null},\"destinationNode\":null},{\"uri\":\"" + e2.getUri() + "\",\"id\":\"" + e2.getId() + "\",\"sourceNode\":null,\"destinationNode\":null}]" ||
        message == "[{\"uri\":\"" + e2.getUri() + "\",\"id\":\"" + e2.getId() + "\",\"sourceNode\":{\"uri\":null,\"id\":null,\"label\":null,\"x\":0.0,\"y\":0.0,\"nodeTypes\":null,\"inParameters\":null,\"outParameters\":null},\"destinationNode\":null},{\"uri\":\"" + e1.getUri() + "\",\"id\":\"" + e1.getId() + "\",\"sourceNode\":null,\"destinationNode\":null}]"
    )
  }

  @Test
  def getViewNodesFails = {
    val id = "someId"
    Mockito.when(service.getViewNodes(id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views/" + id + "/nodes")).andExpect(status.isInternalServerError()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getViewsNodesReturnsEmpty = {
    val id = "someId"
    Mockito.when(service.getViewNodes(id)).thenReturn(Success(Some(Set())))
    val result = mockMvc.perform(get("/views/" + id + "/nodes")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getViewsNodesReturnsSomeNodes = {
    val id = "someId"
    val n1 = new Node()
    val n2 = new Node(null, 0, 0, null, null, null)
    Mockito.when(service.getViewNodes(id)).thenReturn(Success(Some(Set(n1, n2))))
    val result = mockMvc.perform(get("/views/" + id + "/nodes")).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    val n1Str = "{\"uri\":null,\"id\":null,\"label\":null,\"x\":0.0,\"y\":0.0,\"nodeTypes\":null,\"inParameters\":null,\"outParameters\":null}"
    val n2Str = "{\"uri\":\"" + n2.getUri() + "\",\"id\":\"" + n2.getId() + "\",\"label\":null,\"x\":0.0,\"y\":0.0,\"nodeTypes\":null,\"inParameters\":null,\"outParameters\":null}"
    assertTrue(message == "[" + n1Str + "," + n2Str + "]" ||
      message == "[" + n2Str + "," + n1Str + "]")
  }

  @Test
  def getEdgeFails = {
    val id = "someId"
    Mockito.when(service.getEdge(id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views/edges/" + id)).andExpect(status.isInternalServerError()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getEdgeReturnsSomeEdge = {
    val e = new Edge(null, null)
    val id = e.getId()
    Mockito.when(service.getEdge(id)).thenReturn(Success(Some(e)))
    val result = mockMvc.perform(get("/views/edges/" + id)).andExpect(status.isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + e.getUri() + "\",\"id\":\"" + id + "\",\"sourceNode\":null,\"destinationNode\":null}", message)
  }

  import scala.collection.JavaConverters.setAsJavaSetConverter

  @Test
  def getNodeFails = {
    val id = "123-456"
    Mockito.when(service.getNode(id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/views/nodes/" + id)).andExpect(status.isInternalServerError()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getNodeReturnsSomeNode = {
    val types = Set("https://type/1",
      "https://type/2",
      "https://type/3")
    val n = new Node("Label", 1, 2, types.asJava, Set[String]().asJava, Set[String]().asJava)
    val id = n.getId()
    Mockito.when(service.getNode(id)).thenReturn(Success(Some(n)))
    val result = mockMvc.perform(get("/views/nodes/" + id)).andExpect(status().isOk()).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + n.getUri() + "\",\"id\":\"" + id + "\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/1\",\"https://type/2\",\"https://type/3\"],\"inParameters\":[],\"outParameters\":[]}", message)
  }
}