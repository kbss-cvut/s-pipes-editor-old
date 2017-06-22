package cz.cvut.kbss.spipes.service

import cz.cvut.kbss.spipes.model.spipes.{Context, Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.SpipesDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SpipesService {

  @Autowired
  private var spipesDao: SpipesDao = _

  def getModuleTypes(url: String): Option[Traversable[ModuleType]] =
    spipesDao.getModuleTypes(url + "/data")

  def getModules(url: String): Option[Traversable[Module]] =
    spipesDao.getModules(url)

  def getScripts(url: String): Option[Traversable[Context]] =
    spipesDao.getScripts(url)

  def getScript(url: String, id: String): Option[Context] =
    spipesDao.getScripts(url).get.find(_.getUri == id)
}