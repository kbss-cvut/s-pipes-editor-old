package cz.cvut.kbss.sempipes.test.persistence

import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProperties}
import cz.cvut.kbss.sempipes.persistence.PersistenceFactory
import cz.cvut.kbss.sempipes.util.ConfigParam
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@PropertySource(Array("classpath:config.properties"))
@ContextConfiguration(classes = Array(classOf[PersistenceFactory]))
class PersistenceFactoryTest {
  @Autowired
  private var environment: Environment = _
  @Autowired
  private var emf: EntityManagerFactory = _

  @Test
  def testPersistenceInitialization() {
    assertNotNull(emf)
    val em = emf.createEntityManager()
    try
      assertNotNull(em)
    finally
      em.close()
    assertEquals(environment.getProperty(ConfigParam.REPOSITORY_URL.toString), emf.getProperties.get(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY))
  }
}