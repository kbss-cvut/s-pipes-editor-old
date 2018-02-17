package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.Logger
import cz.cvut.kbss.spipes.service.ViewService
import cz.cvut.kbss.spipes.util.Implicits._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
@RestController
@RequestMapping(path = Array("/views"))
class ViewController extends Logger[ViewController] {

  @Autowired
  private var viewService: ViewService = _

  @GetMapping(path = Array("/{script}/new"), produces = Array(JsonLd.MEDIA_TYPE))
  def newFromSpipes(@PathVariable script: String): ResponseEntity[Any] = {
    log.info("Creating a view for script " + script)
    viewService.newViewFromSpipes(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info("View can not be created, no modules found for script " + script)
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(v)) =>
        log.info("View for script " + script + " created")
        log.trace(v)
        new ResponseEntity(v, HttpStatus.OK)
    }
  }
}