package cz.cvut.kbss.sempipes.service

import cz.cvut.kbss.sempipes.model.sempipes.{Context, Module, ModuleType}
import cz.cvut.kbss.sempipes.persistence.dao.SempipesDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SempipesService {

  @Autowired
  private var dataStreamDao: SempipesDao = _

  def getModuleTypes(url: String): Option[Traversable[ModuleType]] =
    dataStreamDao.getModuleTypes(url + "/data")

  def getModules(url: String): Option[Traversable[Module]] =
    dataStreamDao.getModules(url)

  def getScripts(url: String): Option[Traversable[Context]] =
    dataStreamDao.getScripts(url)

  def getScript(url: String, id: String): Option[Context] =
    dataStreamDao.getScripts(url).get.find(_.getUri == id)
}