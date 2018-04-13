package cz.cvut.kbss.spipes.service

import java.io.{File, FileOutputStream}

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.dto.ScriptDTO
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import cz.cvut.kbss.spipes.websocket.WebsocketController
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.impl.PropertyImpl
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
  private var helper: OntologyHelper = _

  @Autowired
  private var scriptDao: ScriptDao = _

  def getModules(filePath: String): Either[Throwable, Option[Traversable[Module]]] = {
    log.info(f"""Looking for modules in $filePath""")
    helper.createUnionModel(new File(filePath)).flatMap(m => {
      scriptDao.getModules(m)
    }) match {
      case Success(i) if !i.isEmpty() =>
        log.info(f"""Modules found in $filePath""")
        Right(Some(i.asScala))
      case Success(_) =>
        log.warn(f"""Modules not found in $filePath""")
        Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e)
        Left(e)
    }
  }


  def getModuleTypes(filePath: String): Either[Throwable, Option[Traversable[ModuleType]]] = {
    log.info(f"""Looking for module types in $filePath""")
    helper.createUnionModel(new File(filePath)).flatMap(m => {
      scriptDao.getModuleTypes(m)
    }) match {
      case Success(i) if !i.isEmpty() =>
        log.info(f"""Module types found in $filePath""")
        Right(Some(i.asScala))
      case Success(_) =>
        log.warn(f"""Module types not found in $filePath""")
        Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e)
        Left(e)
    }
  }

  def getScripts: Option[Seq[ScriptDTO]] = {
    scriptDao.getScriptsWithImports(true) match {
      case Some(i) if i.nonEmpty =>
        Some(
          i.flatMap(
            p => p._2.map(
              f => new ScriptDTO(
                p._1.toURI().relativize(f.toURI()).getPath(),
                f.getAbsolutePath()
              )
            )
          ).toList.sortWith(_.getScriptPath() < _.getScriptPath())
        )
      case _ => None
    }
  }

  def createDependency(scriptPath: String, from: String, to: String): Try[_] = {
    log.info(f"""Creating dependency from $from to $to in $scriptPath""")
    helper.createUnionModel(new File(scriptPath)).flatMap(model => {
      model.listSubjects().asScala.find(_.getURI() == from) ->
        model.listSubjects().asScala.find(_.getURI() == to) match {
        case (Some(moduleFrom), Some(moduleTo)) =>
          val m = ModelFactory.createDefaultModel().read(scriptPath)
          m.add(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
          cleanly(new FileOutputStream(scriptPath))(_.close())(os => {
            m.write(os, "TTL")
          })
        case (None, _) =>
          Failure(new IllegalArgumentException("Source module not found"))
        case (_, None) =>
          Failure(new IllegalArgumentException("Target module not found"))
      }
    })
  }

  def deleteDependency(scriptPath: String, from: String, to: String): Try[_] = {
    log.info(f"""Deleting dependency from $from to $to in $scriptPath""")
    val ontologyUri = // FIXME Find a better way to determine the ontologyUri
      if (from.contains("#"))
        from.split("#").head
      else
        from.reverse.dropWhile(_ != '/').reverse
    val fileName = helper.getFile(ontologyUri).map(_.getAbsolutePath())
      .getOrElse(scriptPath)
    helper.createUnionModel(new File(scriptPath)).flatMap(model => {
      model.listSubjects().asScala.find(_.getURI() == from) ->
        model.listSubjects().asScala.find(_.getURI() == to) match {
        case (Some(moduleFrom), Some(moduleTo)) =>
          val m = ModelFactory.createDefaultModel().read(fileName)
          m.removeAll(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
          cleanly(new FileOutputStream(fileName))(_.close())(os => {
            m.write(os, "TTL")
          })
            .map(_ => WebsocketController.notify(scriptPath))
        case (None, _) =>
          Failure(new IllegalArgumentException("Source module not found"))
        case (_, None) =>
          Failure(new IllegalArgumentException("Target module not found"))
      }
    })
  }

  def deleteModule(scriptPath: String, module: String): Try[_] = {
    log.info(f"""Deleting module $module from $scriptPath""")
    val ontologyUri = // FIXME Find a better way to determine the ontologyUri
      if (module.contains("#"))
        module.split("#").head
      else
        module.reverse.dropWhile(_ != '/').reverse
    val fileName = helper.getFile(ontologyUri).map(_.getAbsolutePath())
      .getOrElse(scriptPath)

    val m = ModelFactory.createDefaultModel().read(fileName)
    m.removeAll(m.getResource(module), null, null)
    m.removeAll(null, null, m.getResource(module))
    cleanly(new FileOutputStream(fileName))(_.close())(os => {
      m.write(os, "TTL")
    })
      .map(_ => WebsocketController.notify(scriptPath))
  }
}