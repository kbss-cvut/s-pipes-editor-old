package cz.cvut.kbss.sempipes.persistence

import javax.annotation.PostConstruct

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.ontodriver.sesame.SesameDataSource
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Component
class DataStreamDao {

  var emf: EntityManagerFactory = _

  @PostConstruct
  def init(): Unit = {
      // create persistence unit
      val props: Map[String, String] = Map(
        // Here we set up basic storage access properties - driver class, physical location of the storage
        JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> "local://temporary",// jopa uses the URI scheme to choose between local and remote repo, file and (http, https and ftp)resp.
        JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "http://temporary",
        JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource",
        // View transactional changes during transaction
        //OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY -> true.toString(),
        // Ontology language
        JOPAPersistenceProperties.LANG -> "en",
        // Where to look for entity classes
        JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.sempipes.model",
        // Persistence provider name
        PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName(),
        SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE -> "true");
      emf = Persistence.createEntityManagerFactory("testPersistenceUnit", props.asJava);
  }

  def createNewContext(): Unit = {

  }

  //TODO stream would be better than url
  def getModel(url: String, callback: (EntityManager) => ()): Unit = {
    // retrieve data from url

    // load data into new temporary JOPA context

    // retrieve JOPA objects by callback function

    // destroy temporary context
  }



}



