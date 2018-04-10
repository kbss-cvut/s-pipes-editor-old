package cz.cvut.kbss.spipes.persistence.dao

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}
import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties
import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.model.Vocabulary._
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Implicits.configParamValue
import cz.cvut.kbss.spipes.util._
import javax.annotation.PostConstruct
import org.apache.jena.rdf.model.Model
import org.eclipse.rdf4j.rio.RDFFormat
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Try

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Repository
class ScriptDao extends PropertySource with Logger[ScriptDao] with ResourceManager {

  var emf: EntityManagerFactory = _

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

  def getModules(m: Model): Try[JList[Module]] =
    get(m)(s_c_Modules)(classOf[Module])

  def getModuleTypes(m: Model): Try[JList[ModuleType]] =
    get(m)(s_c_Module)(classOf[ModuleType])


  private def get[T <: AbstractEntity](m: Model) =
    (owlClass: String) =>
      (resultClass: Class[T]) => {
        val em = emf.createEntityManager()
        val repo = JopaPersistenceUtils.getRepository(em)
        cleanly(repo.getConnection())(c => {
          c.clear()
          c.close()
        })(connection => {
          val writer = new ByteArrayOutputStream()
          m.write(writer, "TTL")
          val reader = new ByteArrayInputStream(writer.toByteArray())
          connection.add(reader, "", RDFFormat.TURTLE)
          emf.getCache().evict(resultClass)
          val query = em.createNativeQuery("select ?s where { ?s a ?type }", resultClass)
            .setParameter("type", URI.create(owlClass))
          query.getResultList()
        })
      }

  def getScripts(ignore: Boolean): Option[Set[(File, Set[File])]] = {
    val scriptsPaths = discoverLocations
    log.info("Looking for any scripts in " + scriptsPaths.mkString("[", ",", "]"))
    scriptsPaths.toSet.map((f: File) => f -> find(f, Set())).filter(_._2.nonEmpty) match {
      case i if i.nonEmpty =>
        if (ignore)
          Some(i.map(p => p._1 -> p._2.diff(ignored)))
        else
          Some(i)
      case _ => None
    }
  }

  lazy val discoverLocations: Array[File] = getProperty(SCRIPTS_LOCATION).split(";").map(new File(_))

  private def find(root: File, acc: Set[File]): Set[File] =
    if (root.isFile() && root.getName().contains(".ttl"))
      acc + root
    else if (root.isDirectory())
      root.listFiles() match {
        case s if s.nonEmpty => s.map((f) => find(f, acc)).reduceLeft(_ ++ _)
        case _ => acc
      }
    else
      acc

  private def ignored = {
    val sc = getProperty(SCRIPTS_LOCATION)
    val ignoreFileName = sc + "/.spipesignore"
    if (new File(ignoreFileName).exists()) {

      val ignored = collection.mutable.Set[File]()

      val lines = Source.fromFile(ignoreFileName).getLines().toSeq

      def flatten(file: File)(acc: Set[File]): Set[File] =
        if (file.isDirectory())
          file.listFiles().map(f => flatten(f)(acc)).reduceLeft(_ ++ _)
        else
          acc + file

      for (l <- lines.filter(_.nonEmpty)) {
        if (l.startsWith("!")) {
          if (l.endsWith("*"))
            if (l.substring(0, l.length() - 1).endsWith("/"))
              ignored --= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l.tail.substring(0, l.length() - 2))))(Set())
            else
              ignored --= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l.tail.substring(0, l.length() - 1))))(Set())
          else
            ignored --= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l.tail)))(Set())
        }
        else {
          if (l.endsWith("*"))
            if (l.substring(0, l.length() - 1).endsWith("/"))
              ignored ++= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l.substring(0, l.length() - 2))))(Set())
            else
              ignored ++= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l.substring(0, l.length() - 1))))(Set())
          else
            ignored ++= flatten(new File(String.format("%s/%s", getProperty(SCRIPTS_LOCATION), l)))(Set())
        }
      }
      ignored
    }
    else
      Set[File]()
  }
}