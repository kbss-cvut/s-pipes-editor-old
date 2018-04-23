package cz.cvut.kbss.spipes.persistence.dao

import java.io.File
import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties
import cz.cvut.kbss.spipes.model.Vocabulary._
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.util._
import javax.annotation.PostConstruct
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Repository
class ScriptDao extends PropertySource with Logger[ScriptDao] with ResourceManager with ScriptManager {

  var emf: EntityManagerFactory = _

  @PostConstruct
  def init: Unit = {
    // create persistence unit
    val props: Map[String, String] = Map(
      // Here we set up basic storage access properties - driver class, physical location of the storage
      JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> "local://temporary", // jopa uses the URI scheme to choose between local and remote repo, file and (http, https and ftp)resp.
      JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "http://temporary",
      JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.jena.JenaDataSource",
      // Ontology language
      JOPAPersistenceProperties.LANG -> Constants.PU_LANGUAGE,
      // Where to look for entity classes
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.spipes.model",
      // Persistence provider name
      PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName(),
      JenaOntoDriverProperties.IN_MEMORY -> true.toString())
    emf = Persistence.createEntityManagerFactory("testPersistenceUnit", props.asJava)
  }

  def getModules(m: Model): Try[JList[Module]] = Try {
    val em = emf.createEntityManager()
    val inferredModel = ModelFactory.createRDFSModel(m)
    val dataset = JopaPersistenceUtils.getDataset(em)
    dataset.setDefaultModel(inferredModel)
    emf.getCache().evict(classOf[Module])
    val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Module])
      .setParameter("type", URI.create(s_c_Modules))
    val modules = query.getResultList()
    modules.forEach(module => {
      val q = em.createNativeQuery(
        f"""
           |prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
           |
           |select distinct ?type where {
           |  ?module a ?type .
           |  filter not exists {
           |    ?module  a ?subtype .
           |    ?subtype rdfs:subClassOf ?type .
           |    filter ( ?subtype != ?type )
           |  }
           |}
         """.stripMargin
        , classOf[ModuleType])
        .setParameter("module", module.getUri())
      val ts = q.getResultList()
      if (ts.size() > 1)
        log.warn(
          f"""More than one most specific type found for module ${module.getUri()}:
             |$ts
           """.stripMargin)
      if (!ts.isEmpty()) {
        module.setSpecificType(ts.get(0))
        log.info(f"""Most specific type for module ${module.getUri()} is ${ts.get(0).getUri()}""")
      }
      else
        log.error(f"""Module ${module.getUri()} has no most specific type""")
    })
    modules
  }

  def getModuleTypes(m: Model): Try[JList[ModuleType]] = Try {
    val em = emf.createEntityManager()
    val inferredModel = ModelFactory.createRDFSModel(m)
    val dataset = JopaPersistenceUtils.getDataset(em)
    dataset.setDefaultModel(inferredModel)
    emf.getCache().evict(classOf[ModuleType])
    val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[ModuleType])
      .setParameter("type", URI.create(s_c_Module))
    query.getResultList()
  }

  def getScripts: Option[Set[File]] = {
    val scriptsPaths = discoverLocations
    log.info("Looking for any scripts in " + scriptsPaths.mkString("[", ",", "]"))
    scriptsPaths.toSet.flatMap((f: File) => find(f, Set())) match {
      case i if i.nonEmpty => Some(i)
      case _ => None
    }
  }

  def getScriptsWithImports: Option[Set[(File, Set[File])]] = {
    val scriptsPaths = discoverLocations
    log.info("Looking for any scripts in " + scriptsPaths.mkString("[", ",", "]"))
    scriptsPaths.toSet.map((f: File) => f -> find(f, Set())).filter(_._2.nonEmpty) match {
      case i if i.nonEmpty =>
        Some(i)
      case _ => None
    }
  }

  private def find(root: File, acc: Set[File]): Set[File] =
    if (ignored.contains(root)) {
      log.info(f"""Ignoring $root""")
      acc
    }
    else {
      if (root.isFile() && root.getName().contains(".ttl"))
        acc + root
      else if (root.isDirectory())
        root.listFiles() match {
          case s if s.nonEmpty => s.map((f) => find(f, acc)).reduceLeft(_ ++ _)
          case _ => acc
        }
      else
        acc
    }
}