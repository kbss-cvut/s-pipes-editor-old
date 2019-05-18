package cz.cvut.kbss.spipes.service

import java.io.{File, FileOutputStream}
import java.util.Collections

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.dto.FunctionDTO
import cz.cvut.kbss.spipes.model.dto.filetree.{FileTree, SubTree}
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import cz.cvut.kbss.spipes.websocket.NotificationController
import org.apache.jena.rdf.model.impl.PropertyImpl
import org.apache.jena.rdf.model.{ModelFactory, ResourceFactory}
import org.apache.jena.util.FileUtils
import org.apache.jena.vocabulary.{RDF, RDFS}
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

  def getFunctions(filePath: String): Either[Throwable, Option[Traversable[FunctionDTO]]] = {
    log.info(f"""Looking for functions in $filePath""")
    scriptDao.getFunctionStatements(ModelFactory.createDefaultModel().read(filePath)) match {
      case Success(i) if i.hasNext() =>
        log.info(f"""Functions found in $filePath""")
        Right(Some(i.asScala.map(st => {
          val s = st.getSubject()
          new FunctionDTO(
            s.getURI(),
            s.getLocalName(),
            if (s.listProperties(RDFS.comment).hasNext())
              Collections.singleton(s.listProperties(RDFS.comment).next().getString())
            else
              null
          )
        })
          .toStream))
      case Success(_) =>
        log.warn(f"""Functions not found in $filePath""")
        Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage(), e)
        Left(e)
    }
  }

  def getModules(filePath: String): Either[Throwable, Option[Traversable[Module]]] = {
    log.info(f"""Looking for modules in $filePath""")
    helper.createOntModel(new File(filePath)).flatMap(m => {
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
    helper.createOntModel(new File(filePath)).flatMap(m => {
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

  def getScripts: FileTree =
    scriptDao.getScriptsTree match {
      case Array(r) => r
      case a => new SubTree(a, "scripts")
    }

  def getScriptsWithFunctions: Option[Set[File]] = scriptDao.getScripts.map(fs =>
    fs.filter(
      helper.createOntModel(_)
        .map(_.contains(null, RDF.`type`, ResourceFactory.createResource("http://topbraid.org/sparqlmotion#Function")))
        .getOrElse(false)
    )
  ) match {
    case Some(s) if s.nonEmpty => Some(s)
    case _ => None
  }

  def createDependency(scriptPath: String, from: String, to: String): Try[_] = {
    log.info(f"""Creating dependency from $from to $to in $scriptPath""")
    helper.createOntModel(new File(scriptPath)).flatMap(model => {
      model.listSubjects().asScala.find(_.getURI() == from) ->
        model.listSubjects().asScala.find(_.getURI() == to) match {
        case (Some(moduleFrom), Some(moduleTo)) =>
          val m = ModelFactory.createDefaultModel().read(scriptPath)
          m.add(moduleFrom, new PropertyImpl(Vocabulary.s_p_next), moduleTo)
          cleanly(new FileOutputStream(scriptPath))(_.close())(os => {
            m.write(os, FileUtils.langTurtle)
          })
            .map(_ => NotificationController.notify(scriptPath))
        case (None, _) =>
          Failure(new IllegalArgumentException("Source module not found"))
        case (_, None) =>
          Failure(new IllegalArgumentException("Target module not found"))
      }
    })
  }

  def deleteDependency(scriptPath: String, from: String, to: String): Try[_] = {
    log.info(f"""Deleting dependency from $from to $to in $scriptPath""")
    helper.getFileDefiningTriple(from, Vocabulary.s_p_next, to)(new File(scriptPath)) match {
      case Success(file) =>
        val m = ModelFactory.createDefaultModel().read(file)
        cleanly(new FileOutputStream(file))(_.close())(os => {
          m.removeAll(m.getResource(from), new PropertyImpl(Vocabulary.s_p_next), m.getResource(to)).write(os, FileUtils.langTurtle)
        })
          .map(_ => NotificationController.notify(scriptPath))
      case failure => failure
    }
  }

  def deleteModule(scriptPath: String, module: String): Try[_] = {
    log.info(f"""Deleting module $module from $scriptPath""")
    helper.getFileDefiningSubject(module)(new File(scriptPath)) match {
      case Success(file) =>
        val m = ModelFactory.createDefaultModel().read(file)
        m.removeAll(m.getResource(module), null, null)
        m.removeAll(null, null, m.getResource(module))
        cleanly(new FileOutputStream(file))(_.close())(os => {
          m.write(os, FileUtils.langTurtle)
        })
          .map(_ => NotificationController.notify(scriptPath))
      case f => f
    }
  }
}