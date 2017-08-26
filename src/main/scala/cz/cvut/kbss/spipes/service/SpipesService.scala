package cz.cvut.kbss.spipes.service

import java.io.FileNotFoundException
import java.util.{List => JList}

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.SpipesDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SpipesService {

  private final val log = LoggerFactory.getLogger(classOf[SpipesService])

  @Autowired
  private var spipesDao: SpipesDao = _

  def getModules(fileName: String): Either[Throwable, Option[Traversable[Module]]] =
    spipesDao.getModules(fileName) match {
      case Success(null) => Right(None)
      case Success(v: JList[Module]) if !v.isEmpty() => Right(Some(v.asScala))
      case Success(v: JList[Module]) => Right(None)
      case Failure(e: FileNotFoundException) => Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage())
        log.error(e.getStackTrace().mkString("\n"))
        Left(e)
    }

  def getModuleTypes(fileName: String): Either[Throwable, Option[Traversable[ModuleType]]] =
    spipesDao.getModuleTypes(fileName) match {
      case Success(null) => Right(None)
      case Success(v: JList[ModuleType]) if !v.isEmpty() => Right(Some(v.asScala))
      case Success(v: JList[ModuleType]) => Right(None)
      case Failure(e: FileNotFoundException) => Right(None)
      case Failure(e) =>
        log.error(e.getLocalizedMessage())
        log.error(e.getStackTrace().mkString("\n"))
        Left(e)
    }
}