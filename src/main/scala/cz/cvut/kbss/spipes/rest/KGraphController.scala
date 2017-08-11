package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.spipes.service.ViewService
import cz.cvut.kbss.spipes.util.ConfigParam.SPIPES_LOCATION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.04.2017.
  */
@RestController
@RequestMapping(path = Array("/views"))
@PropertySource(Array("classpath:config.properties"))
class KGraphController {

  @Autowired
  private var viewService: ViewService = _

  @Autowired
  private var environment: Environment = _

  private val spipesLocation = SPIPES_LOCATION.value


}