package cz.cvut.kbss.sempipes.test.service

import cz.cvut.kbss.sempipes.config.ServiceConfig
import cz.cvut.kbss.sempipes.test.config.{TestPersistenceConfig, TestServiceConfig}
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[ServiceConfig], classOf[TestPersistenceConfig]))
abstract class BaseServiceTestRunner