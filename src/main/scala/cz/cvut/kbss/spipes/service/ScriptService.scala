package cz.cvut.kbss.spipes.service

import java.io.{File, FileInputStream, FileNotFoundException, FileOutputStream}
import java.util.{List => JList}

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.vocabulary.{OWL, RDF}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ScriptService extends PropertySource with Logger[ScriptService] with ResourceManager {

  var ontologyUriMap: Map[String, File] = _

  @Autowired
  private var scriptDao: ScriptDao = _

  def getModules(fileName: String): Either[Throwable, Option[Traversable[Module]]] = {
    log.info("Looking for modules in file " + fileName)
    scriptDao.getModules(false)(fileName) match {
      case Success(ms) =>
        val modules =
          if (ms == null || ms.isEmpty)
            None
          else
            Some(ms.asScala)
        getImports(getProperty(SCRIPTS_LOCATION))(fileName) match {
          case Success(is) =>
            val importedModules = is.flatMap(i => scriptDao.getModules(true)(i) match {
              case Success(imported) =>
                imported.asScala
              case Failure(e) =>
                log.warn(e.getLocalizedMessage(), e)
                Seq()
            })
            modules match {
              case Some(ownModules) =>
                Right(Some(ownModules ++ importedModules))
              case None =>
                if (importedModules.isEmpty)
                  Right(None)
                else
                  Right(Some(importedModules))
            }
          case Failure(e) =>
            log.warn(s"""Failed to find imports for $fileName""", e)
            Right(modules)
        }
      case Failure(_: FileNotFoundException) => Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
        Left(e)
    }
  }

  def getModuleTypes(fileName: String): Either[Throwable, Option[Traversable[ModuleType]]] =
    scriptDao.getModuleTypes(false)(fileName) match {
      case Success(ms) =>
        val types =
          if (ms == null || ms.isEmpty)
            None
          else
            Some(ms.asScala)
        getImports(getProperty(SCRIPTS_LOCATION))(fileName) match {
          case Success(is) =>
            val importedTypes = is.flatMap(i => scriptDao.getModuleTypes(true)(i) match {
              case Success(imported) =>
                imported.asScala
              case Failure(e) =>
                log.warn(e.getLocalizedMessage(), e)
                Seq()
            })
            types match {
              case Some(ownTypes) =>
                Right(Some(ownTypes ++ importedTypes))
              case None =>
                if (importedTypes.isEmpty)
                  Right(None)
                else
                  Right(Some(importedTypes))
            }
          case Failure(e) =>
            log.warn(s"""Failed to find imports for $fileName""", e)
            Right(types)
        }
      case Failure(_: FileNotFoundException) => Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
        Left(e)
    }

  def getScriptNames: Option[Set[String]] = scriptDao.getScripts.map(
    _.filter(f => f.getName().toLowerCase().endsWith(".ttl"))
  ) match {
    case Some(s) if s.nonEmpty =>
      ontologyUriMap = collectOntologyUris(s)
      Some(s.map(_.getName()))
    case _ =>
      None
  }

  def deleteModule(script: String, module: String): Try[Model] = {
    log.info("Deleting module " + module + " from script " + script)
    val fileName = getProperty(SCRIPTS_LOCATION) + "/" + script
    val model = ModelFactory.createDefaultModel()
    model.read(fileName)
    cleanly(new FileOutputStream(fileName))(_.close())(os => {
      model.removeAll(model.getResource(module), null, null)
      model.write(os, "TTL")
    })
  }

  def getOntologyUri(f: File): Option[String] = {
    log.info(s"""Looking for an ontology in file ${f.getName()}""")
    cleanly(new FileInputStream(f))(_.close())(is => {
      val model = ModelFactory.createDefaultModel()
      val st = model.read(is, null, "TTL").listStatements().toList().asScala
      st.find(st => st.getPredicate().eq(RDF.`type`) && st.getObject().eq(OWL.Ontology)).map(_.getSubject().getURI())
    }) match {
      case Success(v) => v
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e)
        None
    }
  }

  def collectOntologyUris(files: Set[File]): Map[String, File] =
    files.map(f => getOntologyUri(f) -> f).filter(_._1.nonEmpty).map(p => p._1.get -> p._2).toMap

  def getImports(rootPath: String): String => Try[mutable.Buffer[String]] = (fileName: String) => {
    log.info(s"""Looking for imports in $fileName""")
    cleanly(new FileInputStream(rootPath + "/" + fileName))(_.close())(is => {
      val model = ModelFactory.createDefaultModel()
      val st = model.read(is, null, "TTL").listStatements().toList().asScala
      st.filter(st => st.getPredicate().eq(OWL.imports)).map(_.getObject().asResource().getURI())
    })
  }
}