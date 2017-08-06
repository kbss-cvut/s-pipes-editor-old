package cz.cvut.kbss.spipes.test.rest

import cz.cvut.kbss.spipes.model.spipes.Context
import cz.cvut.kbss.spipes.service.SpipesService
import cz.cvut.kbss.spipes.util.ConfigParam._
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import scala.util.{Failure, Success}


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
class ScriptControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var env: Environment = _

  @Autowired
  private var service: SpipesService = _

  @Test
  def getScriptsReturnsNothing {
    Mockito.when(service.getScripts(env.getProperty(SPIPES_LOCATION.value) + "/scripts")).thenReturn(Success(Set()))
    val result = mockMvc.perform(get("/scripts")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getScriptsReturnsFailure {
    Mockito.when(service.getScripts(env.getProperty(SPIPES_LOCATION.value) + "/scripts")).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/scripts")).andExpect(status.isInternalServerError).andReturn
    val message = result.getResponse.getContentAsString
    assertTrue(message.isEmpty)
  }

  @Test
  def getScriptReturnsNotFound {
    val id = "someRandomId"
    Mockito.when(service.getScript(env.getProperty(SPIPES_LOCATION.value) + "/contexts", id)).thenReturn(Failure(new Throwable()))
    val result = mockMvc.perform(get("/script/" + id)).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertTrue(message.isEmpty)
  }

  @Test
  def getScriptReturnsScript {
    val context = new Context(null, null)
    val id = context.getId()
    Mockito.when(service.getScript(env.getProperty(SPIPES_LOCATION.value) + "/contexts", id)).thenReturn(Success(Some(context)))
    val result = mockMvc.perform(get("/scripts/" + id)).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"" + context.getUri() + "\",\"id\":\"" + id + "\",\"label\":null,\"comment\":null}", message)
  }
}