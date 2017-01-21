package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.model.sempipes.Context
import cz.cvut.kbss.sempipes.service.SempipesService
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.{Before, Test}
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
class ControllerTest extends BaseControllerTestRunner {

  @Autowired
  var service: SempipesService = _

  @Before
  override def setUp() {
    super.setUp()
    Mockito.reset(service)
  }

  @Test
  def getScriptsReturnsNone() {
    Mockito.when(service.getScripts("")).thenReturn(None)
    val result = mockMvc.perform(get("/rest/scripts")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertTrue(message.contains("Another message"))
  }

  @Test
  def getScriptReturnsNotFound() {
    val id = "someRandomId"
    Mockito.when(service.getScript("", id)).thenReturn(None)
    val result = mockMvc.perform(get("/rest/script/" + id)).andExpect(status.isNotFound).andReturn
    val message = result.getResponse.getContentAsString
    assertEquals("[]", message)
  }

  @Test
  def getScriptReturnsScript() {
    val id = "someRandomId"
    Mockito.when(service.getScript("", id)).thenReturn(Some(new Context()))
    val result = mockMvc.perform(get("/rest/script/" + id)).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    //todo Compare results
  }
}
