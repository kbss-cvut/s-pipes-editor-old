package cz.cvut.kbss.spipes.persistence

import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties._
import cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER
import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProvider}
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Constants
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration, Primary, PropertySource}
import org.springframework.core.env.Environment

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 07.12.16.
  */
@Configuration
@PropertySource(Array("classpath:config.properties"))
class PersistenceFactory {

  private final val log = LoggerFactory.getLogger(classOf[PersistenceFactory])

  private val DEFAULT_PARAMS = initParams

  @Autowired
  private var environment: Environment = _

  private var emf: EntityManagerFactory = _

  @Bean
  @Primary
  def getEntityManagerFactory: EntityManagerFactory = emf

  @PostConstruct
  private def init(): Unit = {
    log.info("Initializing persistence factory")
    val properties = DEFAULT_PARAMS +
      (ONTOLOGY_PHYSICAL_URI_KEY -> environment.getProperty(REPOSITORY_URL.value)) +
      (DATA_SOURCE_CLASS -> environment.getProperty(DRIVER.value))
    emf = Persistence.createEntityManagerFactory("persistenceFactory", properties.asJava)
    log.info("Persistence factory initialized")
  }

  private def initParams = {
    Map[String, String](
      (OntoDriverProperties.ONTOLOGY_LANGUAGE, Constants.PU_LANGUAGE),
      (SCAN_PACKAGE, "cz.cvut.kbss.spipes.model"),
      (JPA_PERSISTENCE_PROVIDER, classOf[JOPAPersistenceProvider].getName)
    )
  }
}