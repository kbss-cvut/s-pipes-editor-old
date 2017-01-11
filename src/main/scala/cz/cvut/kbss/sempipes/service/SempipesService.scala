package cz.cvut.kbss.sempipes.service

import cz.cvut.kbss.sempipes.model.sempipes.ModuleType
import cz.cvut.kbss.sempipes.persistence.dao.DataStreamDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SempipesService {

  @Autowired
  private var dataStreamDao: DataStreamDao = _

  def getModuleTypes(url: String): Option[Traversable[ModuleType]] = {
    dataStreamDao.getModuleTypes(url)
  }
}