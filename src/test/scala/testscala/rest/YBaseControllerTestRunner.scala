package testjava.rest

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[YRestConfig], classOf[YServiceMockConfig]))
@WebAppConfiguration
abstract class YBaseControllerTestRunner {
  @Autowired
  protected var webApplicationContext = null
  protected var mockMvc = null

  @Before
  @throws[Exception]
  def setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build
  }
}
