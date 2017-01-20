package testjava.rest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MvcResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mockito.when
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
  */
class YControllerTest extends YBaseControllerTestRunner {
  @Autowired
  private[rest] val yService = null

  @Before
  @throws[Exception]
  def setUp() {
    super.setUp
    Mockito.reset(yService)
  }

  @Test
  @throws[Exception]
  def test() {
    when(yService.getHelloMessageByService).thenReturn("yourself")
    val result = mockMvc.perform(get("/ycontrolers")).andReturn
    assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse.getStatus))
    val message = result.getResponse.getContentAsString
    assertTrue(message.contains("yourself"))
    System.out.println("Test failed succesfully")
  }
}
