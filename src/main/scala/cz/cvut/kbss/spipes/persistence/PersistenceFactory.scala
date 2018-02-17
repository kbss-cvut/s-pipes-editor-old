package cz.cvut.kbss.spipes.persistence

import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties._
import cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER
import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProvider}
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Constants
import cz.cvut.kbss.spipes.util.Implicits.configParamValue
import cz.cvut.kbss.spipes.{Logger, PropertySource}
import org.springframework.context.annotation.{Bean, Configuration, Primary}

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 07.12.16.
  */
@Configuration
@org.springframework.context.annotation.PropertySource(Array("classpath:config.properties"))
class PersistenceFactory extends PropertySource with Logger[PersistenceFactory] {

  private var emf: EntityManagerFactory = _

  @Bean
  @Primary
  def getEntityManagerFactory: EntityManagerFactory = emf

  @PostConstruct
  private def init(): Unit = {
    log.info("Initializing persistence factory")
    val properties = initParams +
      (ONTOLOGY_PHYSICAL_URI_KEY -> getProperty(REPOSITORY_URL)) +
      (DATA_SOURCE_CLASS -> getProperty(DRIVER))
    emf = Persistence.createEntityManagerFactory("persistenceFactory", properties.asJava)
    log.info("Persistence factory initialized")
  }

  private def initParams = {
    Map[String, String](
      (OntoDriverProperties.ONTOLOGY_LANGUAGE, Constants.PU_LANGUAGE),
      (SCAN_PACKAGE, "cz.cvut.kbss.spipes.model"),
      (JPA_PERSISTENCE_PROVIDER, classOf[JOPAPersistenceProvider].getName())
    )
  }
}