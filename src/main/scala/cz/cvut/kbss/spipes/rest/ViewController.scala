package cz.cvut.kbss.spipes.rest


import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.service.ViewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/views"))
class ViewController {

  @Autowired
  private var viewService: ViewService = _

  @GetMapping(path = Array("/{script}/new"), produces = Array(JsonLd.MEDIA_TYPE))
  def newFromSpipes(@PathVariable script: String): ResponseEntity[Any] = {
    viewService.newViewFromSpipes(script) match {
      case Left(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) => new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(v)) => new ResponseEntity(v, HttpStatus.OK)
    }
  }
}