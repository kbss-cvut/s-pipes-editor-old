package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.test.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, ResponseBody, RestController}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
@RestController
@RequestMapping(path = Array("/testController"))
class TestController {

  @Autowired
  private var service: TestService = _

  @GetMapping
  @ResponseBody
  def getMessage: String = service.getMessage()
}
