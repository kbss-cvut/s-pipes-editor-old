package cz.cvut.kbss.sempipes.test.rest

import java.net.URI

import cz.cvut.kbss.sempipes.model.sempipes.Context
import cz.cvut.kbss.sempipes.service.SempipesService
import cz.cvut.kbss.sempipes.util.ConfigParam
import cz.cvut.kbss.sempipes.util.ConfigParam._
import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
class ScriptControllerTest extends BaseControllerTestRunner {

  @Autowired
  var service: SempipesService = _

  @Before
  override def setUp() {
    super.setUp()
    Mockito.reset(service)
  }

  @Test
  def getScriptsReturnsNone() {
    Mockito.when(service.getScripts(ConfigParam.SEMPIPES_LOCATION + "/scripts")).thenReturn(None)
    val result = mockMvc.perform(get("/scripts")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getScriptReturnsNotFound() {
    val id = "someRandomId"
    Mockito.when(service.getScript(SEMPIPES_LOCATION + "/contexts", id)).thenReturn(None)
    val result = mockMvc.perform(get("/script/" + id)).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("", message)
  }

  @Test
  def getScriptReturnsScript() {
    val id = "someRandomId"
    val context = new Context()
    context.setUri(URI.create("https://context"))
    context.setLabel("label")
    context.setComment("comment")
    Mockito.when(service.getScript(SEMPIPES_LOCATION + "/contexts", id)).thenReturn(Some(context))
    val result = mockMvc.perform(get("/scripts/" + id)).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("{\"uri\":\"https://context\",\"label\":\"label\",\"comment\":\"comment\"}", message)
  }
}
