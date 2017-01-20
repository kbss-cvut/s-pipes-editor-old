package testjava.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
  */
@RestController
@RequestMapping(value = "/ycontrolers") class YController {
  @Autowired private[rest] val service = null

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  def getHelloMessageByController: Nothing = service.getHelloMessageByService
}
