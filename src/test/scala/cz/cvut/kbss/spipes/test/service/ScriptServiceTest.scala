package cz.cvut.kbss.spipes.test.service

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.service.{OntologyHelper, ScriptService}
import cz.cvut.kbss.spipes.test.config.ScriptTestServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.08.2017.
  */
@ContextConfiguration(classes = Array(classOf[ScriptTestServiceConfig]))
trait ScriptServiceTest {

  @Autowired
  protected var dao: ScriptDao = _

  @Autowired
  protected var helper: OntologyHelper = _

  @Autowired
  protected var service: ScriptService = _

  protected val fileName = ""
}