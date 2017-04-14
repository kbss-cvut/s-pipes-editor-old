package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.test.persistence.TestPersistenceFactory
import org.springframework.context.annotation.{ComponentScan, Configuration, Import}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 24.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.persistence.dao"))
@Import(Array(classOf[TestPersistenceFactory]))
class TestPersistenceConfig