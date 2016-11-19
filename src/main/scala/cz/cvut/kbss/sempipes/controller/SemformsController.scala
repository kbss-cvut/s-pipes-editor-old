package cz.cvut.kbss.sempipes.controller


import cz.cvut.kbss.jsonld.JsonLd
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.11.16.
  */
@RestController
class SemformsController {

  @RequestMapping(path = Array("/semforms"), consumes = Array(JsonLd.MEDIA_TYPE), produces = Array(JsonLd.MEDIA_TYPE))
  def generateForm(@RequestBody pipeline: String) = {
    new ResponseEntity(pipeline, HttpStatus.OK)
  }

}
