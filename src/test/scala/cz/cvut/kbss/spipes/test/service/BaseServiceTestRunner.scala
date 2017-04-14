package cz.cvut.kbss.spipes.test.service

import cz.cvut.kbss.spipes.test.config.TestServiceConfig
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.01.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestServiceConfig]))
abstract class BaseServiceTestRunner