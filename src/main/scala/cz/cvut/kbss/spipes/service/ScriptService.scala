package cz.cvut.kbss.spipes.service

import java.io.{File, FileInputStream, FileNotFoundException, FileOutputStream}
import java.util.{List => JList}

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.vocabulary.{OWL, RDF, RDFS}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ScriptService extends PropertySource with Logger[ScriptService] with ResourceManager {

  @Autowired
  private var scriptDao: ScriptDao = _

  def getModules(fileName: String): Either[Throwable, Option[Traversable[Module]]] = {
    log.info("Looking for modules in file " + fileName)
    scriptDao.getModules(fileName) match {
      case Success(null) =>
        log.info("No modules found in file " + fileName)
        Right(None)
      case Success(v: JList[Module]) if !v.isEmpty() =>
        log.info("Found modules in file " + fileName)
        log.trace(v.asScala)
        Right(Some(v.asScala))
      case Success(_: JList[Module]) =>
        log.info("No modules found in file " + fileName)
        Right(None)
      case Failure(_: FileNotFoundException) =>
        log.warn("File " + fileName + " not found")
        Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
        Left(e)
    }
  }

  def getModuleTypes(fileName: String): Either[Throwable, Option[Traversable[ModuleType]]] =
    scriptDao.getModuleTypes(fileName) match {
      case Success(null) => Right(None)
      case Success(v: JList[ModuleType]) if !v.isEmpty() => Right(Some(v.asScala))
      case Success(_: JList[ModuleType]) => Right(None)
      case Failure(_: FileNotFoundException) => Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e.getStackTrace().mkString("\n"))
        Left(e)
    }

  def getScriptNames: Option[Set[String]] = scriptDao.getScripts.map(
    _.filter(f => f.getName().toLowerCase().endsWith(".ttl"))
  ) match {
    case Some(s) if s.nonEmpty =>
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

  def getOntologyUri(f: File): Try[Option[String]] = {
    log.info(s"""Looking for an ontology in file ${f.getName()}""")
    val model = ModelFactory.createDefaultModel()
    cleanly(new FileInputStream(f))(_.close())(is => {
      val st = model.read(is, null, "TTL").listStatements().toList().asScala
      st.find(st => st.getPredicate().equals(RDF.`type`) && st.getObject().equals(OWL.Ontology)).map(_.getSubject().getURI())
    })
  }
}