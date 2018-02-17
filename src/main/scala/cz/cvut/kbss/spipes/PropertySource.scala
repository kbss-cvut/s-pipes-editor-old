package cz.cvut.kbss.spipes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 17.02.2018.
  */
trait PropertySource {

  @Autowired
  private var environment: Environment = _

  def getProperty(p: String): String = environment.getProperty(p)
}
