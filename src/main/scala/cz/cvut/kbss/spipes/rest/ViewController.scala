package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.dto.ScriptDTO
import cz.cvut.kbss.spipes.service.ViewService
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.Logger
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

  @PostMapping(path = Array("/new"), produces = Array(JsonLd.MEDIA_TYPE))
  def newFromSpipes(@RequestBody dto: ScriptDTO): ResponseEntity[Any] = {
    val script = dto.getScriptPath()
    log.info("Creating a view for scriptPath " + script)
    viewService.newViewFromSpipes(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info("View can not be created, no modules found for scriptPath " + script)
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(v)) =>
        log.info("View for scriptPath " + script + " created")
        log.trace(v)
        new ResponseEntity(v, HttpStatus.OK)
    }
  }
}