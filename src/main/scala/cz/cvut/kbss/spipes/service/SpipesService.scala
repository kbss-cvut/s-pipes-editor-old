package cz.cvut.kbss.spipes.service

import cz.cvut.kbss.spipes.model.spipes.{Context, Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.SpipesDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SpipesService {

  @Autowired
  private var spipesDao: SpipesDao = _

  def getModuleTypes(fileName: String): Try[Traversable[ModuleType]] =
    spipesDao.getModuleTypes(fileName)

  def getModules(url: String): Try[Traversable[Module]] =
    spipesDao.getModules(url)

  def getScripts(url: String): Try[Traversable[Context]] =
    spipesDao.getScripts(url)

  def getScript(url: String, id: String): Try[Option[Context]] =
    spipesDao.getScripts(url) match {
      case Success(s) => Success(s.find(_.getUri == id))
      case Failure(e) => Failure(e)
    }
}