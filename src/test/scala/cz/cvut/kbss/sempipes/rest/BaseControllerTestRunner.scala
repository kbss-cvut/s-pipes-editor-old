package cz.cvut.kbss.sempipes.rest

import cz.cvut.kbss.sempipes.config.TestServiceConfig
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestServiceConfig]))
@WebAppConfiguration
class BaseControllerTestRunner