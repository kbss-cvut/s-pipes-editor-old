package cz.cvut.kbss.spipes.test.persistence

import cz.cvut.kbss.spipes.test.config.TestPersistenceConfig
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestPersistenceConfig]))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
abstract class BaseDaoTestRunner