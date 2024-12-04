package cz.cvut.kbss.spipes.test.persistence.dao

import java.net.URI

import cz.cvut.kbss.spipes.config.PersistenceConfig
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import org.junit.{Ignore, Test}
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
 *
 * Not used so I guess it is not working properly
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceConfig]))
class AbstractDaoTest {


  @Test
  @Ignore
  def getTest: Unit = {
    //not working
  }

}

