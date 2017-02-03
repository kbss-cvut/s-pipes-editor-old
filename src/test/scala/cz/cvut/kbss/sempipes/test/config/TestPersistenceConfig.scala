package cz.cvut.kbss.sempipes.test.config

import cz.cvut.kbss.sempipes.test.persistence.TestPersistenceFactory
import org.springframework.context.annotation.{ComponentScan, Configuration, Import}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes.persistence.dao"))
@Import(Array(classOf[TestPersistenceFactory]))
class TestPersistenceConfig