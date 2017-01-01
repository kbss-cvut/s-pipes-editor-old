package cz.cvut.kbss.sempipes.persistence

import java.io.File
import java.net.URI
import java.nio.file.Files
import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties._
import cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER
import cz.cvut.kbss.jopa.model.{EntityManagerFactory, JOPAPersistenceProperties, JOPAPersistenceProvider, PersistenceProperties}
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.sempipes.util.ConfigParam._
import cz.cvut.kbss.sempipes.util.Constants
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

object PersistenceFactory {
  val LOG = LoggerFactory.getLogger(classOf[PersistenceFactory])

  val REPOSITORY_FILE_NAME = "sempipes.ttl"
  val FILE_SCHEMA = "file://"

  var initialized = false

  var emf: EntityManagerFactory = _

  def init(ontologyFile: String): Unit = {
    val props: Map[String, String] = Map(
      // Here we set up basic storage access properties - driver class, physical location of the storage
      JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> setupRepository(ontologyFile),
      JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data",
      JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource",
      // View transactional changes during transaction
      OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY -> true.toString(),
      // Ontology language
      JOPAPersistenceProperties.LANG -> "en",
      // Where to look for entity classes
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.sempipes.model",
      // Persistence provider name
      PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName())
    emf = Persistence.createEntityManagerFactory("jopaExample02PU", props.asJava)
    initialized = true
  }

  private def setupRepository(ontologyFile: String) = {
    LOG.debug("Setting up repository...")
    val ontologyFileAbsolute = resolveAbsolutePath(ontologyFile)
    val repoFolder = ontologyFileAbsolute.substring(0, ontologyFileAbsolute.lastIndexOf(File.separatorChar))
    val repoFile = new File(repoFolder + File.separator + REPOSITORY_FILE_NAME)
    if (repoFile.exists()) {
      LOG.debug("Repository already exists. Removing it...")
    }
    try {
      LOG.debug("Copying ontology to the repository...")
      Files.copy(new File(ontologyFileAbsolute).toPath(), repoFile.toPath())
    } catch {
      case e: Throwable =>
        LOG.error("Unable to copy ontology into the repository", e)
        System.exit(1)
    }
    URI.create(FILE_SCHEMA + repoFile.getAbsolutePath()).toString()
  }

  private def resolveAbsolutePath(ontologyFile: String): String = {
    val file = new File(ontologyFile)
    require(file.exists())
    file.getAbsolutePath()
  }

  def createEntityManager() = emf.createEntityManager()

  def close() {
    emf.close()
  }
}