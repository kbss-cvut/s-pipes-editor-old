package cz.cvut.kbss.spipes.service

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import org.apache.jena.rdf.model.impl.PropertyImpl
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ScriptService extends PropertySource with Logger[ScriptService] with ResourceManager {

  @Autowired
  private var helper: OntologyHelper = _

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
        helper.getURIOfImportedOntologies(getProperty(SCRIPTS_LOCATION))(fileName) match {
          case Success(is) =>
            val importedModules = is.flatMap(i => {
              helper.getFile(i).map(f => {
                scriptDao.getModules(true)(f.getAbsolutePath()) match {
                  case Success(imported) =>
                    imported.asScala
                  case Failure(e) =>
                    log.warn(e.getLocalizedMessage(), e)
                    Seq()
                }
              }).getOrElse(Seq())
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
        helper.getURIOfImportedOntologies(getProperty(SCRIPTS_LOCATION))(fileName) match {
          case Success(is) =>
            val importedTypes = is.flatMap(i => {
              val fi = helper.getFile(i)
              fi.map(f => {
                val tts = scriptDao.getModuleTypes(true)(f.getAbsolutePath())
                tts match {
                  case Success(imported) =>
                    imported.asScala
                  case Failure(e) =>
                    log.warn(e.getLocalizedMessage(), e)
                    Seq()
                }
              }).getOrElse(Seq())
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
      Some(s.map(_.getName()))
    case _ =>
      None
  }

  def createDependency(script: String, from: String, to: String): Try[Model] = {
    val fileName = getProperty(SCRIPTS_LOCATION) + "/" + script
    val model = ModelFactory.createDefaultModel()
    model.read(fileName)
    model.listSubjects().asScala.find(_.getURI() == from) ->
      model.listSubjects().asScala.find(_.getURI() == to) match {
      case (Some(moduleFrom), Some(moduleTo)) =>
        model.add(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
        cleanly(new FileOutputStream(fileName))(_.close())(os => model.write(os, "TTL"))
      case (None, _) =>
        Failure(new IllegalArgumentException("Source module not found"))
      case (_, None) =>
        Failure(new IllegalArgumentException("Target module not found"))
    }
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
}