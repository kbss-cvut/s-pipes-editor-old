package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.spipes.service.ViewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

import scala.util.Success

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.04.2017.
  */
@RestController
@RequestMapping(path = Array("/json"))
class KGraphController {

  @Autowired
  private var viewService: ViewService = _

  @GetMapping(path = Array("/new"), produces = Array("application/json"))
  def createFromSpipesJson: ResponseEntity[Any] = {
    val kg = viewService.createJsonFromSpipes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data")
    kg match {
      case Success(g) => new ResponseEntity(g, HttpStatus.OK)
      case _ => new ResponseEntity[Any]("KGraph can not be generated", HttpStatus.BAD_REQUEST)
    }
  }
}