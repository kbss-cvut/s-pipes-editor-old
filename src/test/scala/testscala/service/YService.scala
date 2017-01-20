package testjava.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
  */
@Service
class YService {
  @Autowired
  private[service] val dao = null

  def getHelloMessageByService: Nothing = dao.getHelloMessageByDao
}
