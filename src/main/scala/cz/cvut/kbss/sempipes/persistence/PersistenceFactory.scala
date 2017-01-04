package cz.cvut.kbss.sempipes.persistence

import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties._
import cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER
import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProvider}
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.sempipes.util.ConfigParam._
import cz.cvut.kbss.sempipes.util.Constants
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
  private val DEFAULT_PARAMS = initParams

  @Autowired
  private var environment: Environment = _

  private var emf: EntityManagerFactory = _

  @Bean
  @Primary
  def getEntityManagerFactory: EntityManagerFactory = emf

  @PostConstruct
  private def init(): Unit = {
    val properties = DEFAULT_PARAMS +
      (ONTOLOGY_PHYSICAL_URI_KEY -> environment.getProperty(REPOSITORY_URL.toString)) +
      (DATA_SOURCE_CLASS -> environment.getProperty(DRIVER.toString))
    emf = Persistence.createEntityManagerFactory("persistenceFactory", properties.asJava)
  }

  private def initParams = {
    Map[String, String](
      (OntoDriverProperties.ONTOLOGY_LANGUAGE, Constants.PU_LANGUAGE),
      (SCAN_PACKAGE, "cz.cvut.kbss.sempipes.model"),
      (JPA_PERSISTENCE_PROVIDER, classOf[JOPAPersistenceProvider].getName)
    )
  }
}