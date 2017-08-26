package cz.cvut.kbss.spipes.rest

import java.util.{Set => JSet}

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.service.SpipesService
import cz.cvut.kbss.spipes.util.ConfigParam.SPIPES_LOCATION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping, RestController}

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 05.01.17.
  */
@RestController
@RequestMapping(path = Array("/scripts"))
@PropertySource(Array("classpath:config.properties"))
class ScriptController {

  @Autowired
  private var environment: Environment = _

  @Autowired
  private var service: SpipesService = _

  private val spipesLocation = SPIPES_LOCATION.value

  @GetMapping(path = Array("/{id}/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@PathVariable id: String): ResponseEntity[Any] = {
    service.getModuleTypes(id) match {
      case Left(e) =>
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(types)) =>
        new ResponseEntity(types.toList.sortBy(_.getLabel()).asJava, HttpStatus.OK)
    }
  }

  @GetMapping(path = Array("/{id}/modules"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModules(@PathVariable id: String): ResponseEntity[Any] = {
    service.getModules(id) match {
      case Left(e) =>
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(modules)) =>
        new ResponseEntity(modules.toSet.asJava, HttpStatus.OK)
    }
  }
}