package cz.cvut.kbss.spipes.persistence.dao

import java.io.File
import java.net.URI
import java.util.{List => JList}
import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties
import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.{Constants, JopaPersistenceUtils}
import org.eclipse.rdf4j.rio.RDFFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Try

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Repository
class ScriptDao {

  @Autowired
  private var env: Environment = _

  var emf: EntityManagerFactory = _

  private val scriptsLocation = SCRIPTS_LOCATION.value

  @PostConstruct
  def init: Unit = {
    // create persistence unit
    val props: Map[String, String] = Map(
      // Here we set up basic storage access properties - driver class, physical location of the storage
      JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> "local://temporary", // jopa uses the URI scheme to choose between local and remote repo, file and (http, https and ftp)resp.
      JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "http://temporary",
      JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.sesame.SesameDataSource",
      // View transactional changes during transaction
      OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY -> true.toString(),
      // Ontology language
      JOPAPersistenceProperties.LANG -> Constants.PU_LANGUAGE,
      // Where to look for entity classes
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.spipes.model",
      // Persistence provider name
      PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName(),
      SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE -> true.toString())
    emf = Persistence.createEntityManagerFactory("testPersistenceUnit", props.asJava)
  }

  def getModules(fileName: String): Try[JList[Module]] = {
    val filePath = env.getProperty(scriptsLocation) + "/" + fileName
    val em = emf.createEntityManager()
    Try {

      //TODO load data into NEW TEMPORARY JOPA context
      val repo = JopaPersistenceUtils.getRepository(em)
      repo.getConnection().add(Source.fromFile(filePath).reader(), "http://temporary", RDFFormat.TURTLE)

      // retrieve JOPA objects by callback function

      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Module])
        .setParameter("type", URI.create(Vocabulary.s_c_Modules))
      query.getResultList()
    }
  }

  def getModuleTypes(fileName: String): Try[JList[ModuleType]] = {
    val filePath = env.getProperty(scriptsLocation) + "/" + fileName
    val em = emf.createEntityManager()
    Try {

      //TODO load data into NEW TEMPORARY JOPA context
      val repo = JopaPersistenceUtils.getRepository(em)
      val connection = repo.getConnection()
      connection.add(Source.fromFile(filePath).reader(), "http://temporary", RDFFormat.TURTLE)

      // retrieve JOPA objects by callback function

      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[ModuleType])
        .setParameter("type", URI.create(Vocabulary.s_c_Module))
      val res = query.getResultList()

      connection.clear()
      connection.close()

      res
    }
  }

  def getScripts: Option[Seq[File]] =
    Option(new File(env.getProperty(scriptsLocation)).listFiles())
      .map(_.filterNot(_.isDirectory()))
}