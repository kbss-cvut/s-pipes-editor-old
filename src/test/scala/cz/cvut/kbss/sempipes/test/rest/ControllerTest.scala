package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.test.service.TestService
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.{Before, Test}
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
class ControllerTest extends BaseControllerTestRunner {

  @Autowired
  var service: TestService = _

  @Before
  override def setUp() {
    super.setUp()
    Mockito.reset(service)
  }

  @Test
  def test() {
    Mockito.when(service.getMessage()).thenReturn("Another message")
    val result = mockMvc.perform(get("/testController")).andExpect(status.isOk).andReturn
    val message = result.getResponse.getContentAsString
    assertTrue(message.contains("Another message"))
    System.out.println("Test failed successfully")
  }
}
