package cz.cvut.kbss.sempipes.test.service

import cz.cvut.kbss.sempipes.test.dao.TestDao
import org.springframework.beans.factory.annotation.Autowired

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
@TestService
class TestService {
  @Autowired
  private var dao: TestDao = _

  def getMessage(): String = dao.getMessage()
}
