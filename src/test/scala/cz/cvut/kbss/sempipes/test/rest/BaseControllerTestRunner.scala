package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.test.config.{TestRestConfig, TestServiceConfig}
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestRestConfig]))
@WebAppConfiguration
abstract class BaseControllerTestRunner {

  @Autowired
  protected var webApplicationContext: WebApplicationContext = _
  protected var mockMvc: MockMvc = _

  @Before
  def setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build
  }
}