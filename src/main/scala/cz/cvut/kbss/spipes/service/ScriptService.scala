package cz.cvut.kbss.spipes.service

import java.io.{File, FileOutputStream}

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
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

  def getModules(fileName: String): Either[Throwable, Option[Traversable[Module]]] =
    helper.createModel(new File(s"""${getProperty(SCRIPTS_LOCATION)}/$fileName""")).flatMap(m => {
      scriptDao.getModules(helper.appendImports(m))
    }) match {
      case Success(i) if !i.isEmpty() =>
        Right(Some(i.asScala))
      case Success(_) => Right(None)
      case Failure(e) => Left(e)
    }


  def getModuleTypes(fileName: String): Either[Throwable, Option[Traversable[ModuleType]]] =
    helper.createModel(new File(s"""${getProperty(SCRIPTS_LOCATION)}/$fileName""")).flatMap(m => {
      scriptDao.getModuleTypes(helper.appendImports(m))
    }) match {
      case Success(i) if !i.isEmpty() =>
        Right(Some(i.asScala))
      case Success(_) => Right(None)
      case Failure(e) => Left(e)
    }

  def getScriptNames: Option[Set[String]] = scriptDao.getScripts(true).map(
    _.filter(f => f.getName().toLowerCase().endsWith(".ttl"))
  ) match {
    case Some(s) if s.nonEmpty =>
      Some(s.map(f => new File(getProperty(SCRIPTS_LOCATION)).toURI().relativize(f.toURI()).getPath()))
    case _ =>
      None
  }

  def createDependency(scriptPath: String, from: String, to: String): Try[_] = {
    val fileName = f"""${getProperty(SCRIPTS_LOCATION)}/$scriptPath"""
    helper.getUnionModel(new File(fileName)).flatMap(model => {
      model.listSubjects().asScala.find(_.getURI() == from) ->
        model.listSubjects().asScala.find(_.getURI() == to) match {
        case (Some(moduleFrom), Some(moduleTo)) =>
          val m = ModelFactory.createDefaultModel().read(fileName)
          m.add(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
          cleanly(new FileOutputStream(fileName))(_.close())(os => {
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
    val defaultFilePath = f"""${getProperty(SCRIPTS_LOCATION)}/$scriptPath"""
    val ontologyUri =
      if (from.contains("#"))
        from.split("#").head
      else
        from.reverse.dropWhile(_ != '/').reverse
    val fileName = helper.getFile(ontologyUri).map(_.getAbsolutePath())
      .getOrElse(defaultFilePath)
    helper.getUnionModel(new File(defaultFilePath)).flatMap(model => {
      model.listSubjects().asScala.find(_.getURI() == from) ->
        model.listSubjects().asScala.find(_.getURI() == to) match {
        case (Some(moduleFrom), Some(moduleTo)) =>
          val m = ModelFactory.createDefaultModel().read(fileName)
          m.removeAll(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
          cleanly(new FileOutputStream(fileName))(_.close())(os => {
            m.write(os, "TTL")
          })
            .map(_ => WebsocketController.notify(defaultFilePath))
        case (None, _) =>
          Failure(new IllegalArgumentException("Source module not found"))
        case (_, None) =>
          Failure(new IllegalArgumentException("Target module not found"))
      }
    })
  }

  def deleteModule(scriptPath: String, module: String): Try[_] = {
    val defaultFilePath = f"""${getProperty(SCRIPTS_LOCATION)}/$scriptPath"""
    val ontologyUri =
      if (module.contains("#"))
        module.split("#").head
      else
        module.reverse.dropWhile(_ != '/').reverse
    val fileName = helper.getFile(ontologyUri).map(_.getAbsolutePath())
      .getOrElse(defaultFilePath)

    val m = ModelFactory.createDefaultModel().read(fileName)
    m.removeAll(m.getResource(module), null, null)
    m.removeAll(null, null, m.getResource(module))
    cleanly(new FileOutputStream(fileName))(_.close())(os => {
      m.write(os, "TTL")
    })
      .map(_ => WebsocketController.notify(defaultFilePath))
  }
}