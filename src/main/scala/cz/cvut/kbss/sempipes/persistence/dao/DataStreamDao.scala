package cz.cvut.kbss.sempipes.persistence.dao

import java.io.ByteArrayInputStream
import java.net.URI
import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties
import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.sempipes.{Module, ModuleType}
import cz.cvut.kbss.sempipes.util.JopaPersistenceUtils
import org.openrdf.rio.RDFFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod}
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

import scala.collection.JavaConverters._

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Repository
class DataStreamDao {

  var emf: EntityManagerFactory = _

  @Autowired
  private var restTemplate: RestTemplate = _

  @PostConstruct
  def init(): Unit = {
    // create persistence unit
    val props: Map[String, String] = Map(
      // Here we set up basic storage access properties - driver class, physical location of the storage
      JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> "local://temporary", // jopa uses the URI scheme to choose between local and remote repo, file and (http, https and ftp)resp.
      JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "http://temporary",
      JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.sesame.SesameDataSource",
      // View transactional changes during transaction
      OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY -> true.toString(),
      // Ontology language
      JOPAPersistenceProperties.LANG -> "en",
      // Where to look for entity classes
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.sempipes.model",
      // Persistence provider name
      PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName(),
      SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE -> "true")
    emf = Persistence.createEntityManagerFactory("testPersistenceUnit", props.asJava)
  }

  def createNewContext(): Unit = {

  }

  def getModuleTypes(url: String): Option[Traversable[ModuleType]] = {
    // retrieve data from url
    val uri = URI.create(url)
    val headers = new HttpHeaders()
    headers.set(HttpHeaders.ACCEPT, "text/turtle")
    val entity = new HttpEntity[String](null, headers)
    val is = new ByteArrayInputStream(restTemplate.exchange(uri,
      HttpMethod.GET,
      entity,
      classOf[String]).getBody().getBytes())

    val em = emf.createEntityManager()

    try {
      //TODO load data into NEW TEMPORARY JOPA context
      val repo = JopaPersistenceUtils.getRepository(em)
      repo.getConnection().add(is, "http://temporary", RDFFormat.TURTLE)

      // retrieve JOPA objects by callback function

      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[ModuleType])
        .setParameter("type", URI.create(Vocabulary.s_c_Module))
      query.getResultList() match {
        case l: java.util.List[ModuleType] if !l.isEmpty =>
          Some(l.asScala)
        case _ => None
      }
    }
    finally {
      // TODO destroy temporary context
    }
  }

  def getModules(url: String): Option[Traversable[Module]] = {
    // retrieve data from url
    val uri = URI.create(url)
    val headers = new HttpHeaders()
    headers.set(HttpHeaders.ACCEPT, "text/turtle")
    val entity = new HttpEntity[String](null, headers)
    val is = new ByteArrayInputStream(restTemplate.exchange(uri,
      HttpMethod.GET,
      entity,
      classOf[String]).getBody().getBytes())

    val em = emf.createEntityManager()

    try {
      //TODO load data into NEW TEMPORARY JOPA context
      val repo = JopaPersistenceUtils.getRepository(em)
      repo.getConnection().add(is, "http://temporary", RDFFormat.TURTLE)

      // retrieve JOPA objects by callback function

      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Module])
        .setParameter("type", URI.create(Vocabulary.s_c_Modules))
      query.getResultList() match {
        case l: java.util.List[Module] if !l.isEmpty =>
          Some(l.asScala)
        case _ => None
      }
    }
    finally {
      // TODO destroy temporary context
    }
  }
}