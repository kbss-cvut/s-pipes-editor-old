package cz.cvut.kbss.sempipes.test.persistence

import javax.annotation.{PostConstruct, PreDestroy}

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER
import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProperties, JOPAPersistenceProvider}
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties
import cz.cvut.kbss.sempipes.util.ConfigParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration, Primary, PropertySource}
import org.springframework.core.env.Environment

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.02.17.
  */

@Configuration
@PropertySource(Array("classpath:config.properties"))
class TestPersistenceFactory {

  @Autowired
  private var environment: Environment = _

  @Bean
  private var emf: EntityManagerFactory = _

  @PostConstruct
  private def init() {
    val initProperties = TestPersistenceFactory.getDefaultProperties +
      (JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> environment.getProperty(TestPersistenceFactory.URL_PROPERTY)) +
      (JOPAPersistenceProperties.DATA_SOURCE_CLASS -> environment.getProperty(TestPersistenceFactory.DRIVER_PROPERTY))
    val properties =
      if (environment.getProperty(TestPersistenceFactory.USERNAME_PROPERTY) != null)
        initProperties +
          (JOPAPersistenceProperties.DATA_SOURCE_USERNAME -> environment.getProperty(TestPersistenceFactory.USERNAME_PROPERTY)) +
          (JOPAPersistenceProperties.DATA_SOURCE_PASSWORD -> environment.getProperty(TestPersistenceFactory.PASSWORD_PROPERTY))
      else initProperties

    this.emf = Persistence.createEntityManagerFactory("emf", properties.asJava)
  }

  @PreDestroy
  private def close() {
    if (emf.isOpen) {
      emf.close()
    }
  }
}

object TestPersistenceFactory {
  private val URL_PROPERTY = "test." + ConfigParam.REPOSITORY_URL
  private val DRIVER_PROPERTY = "test." + ConfigParam.DRIVER.toString
  private val USERNAME_PROPERTY = "test.username"
  private val PASSWORD_PROPERTY = "test.password"

  private def getDefaultProperties = {
    Map[String, String](
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.reporting.model",
      SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE -> true.toString,
      SesameOntoDriverProperties.SESAME_USE_INFERENCE -> false.toString,
      JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName
    )
  }
}
