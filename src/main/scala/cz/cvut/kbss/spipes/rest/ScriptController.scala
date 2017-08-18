package cz.cvut.kbss.spipes.rest

import java.io.FileNotFoundException
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
import scala.util.{Failure, Success}

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

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getScripts: ResponseEntity[Any] = service.getScripts(environment.getProperty(spipesLocation) + "/scripts") match {
    case Success(Seq()) => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
    case Success(contexts) => new ResponseEntity(contexts.toSet.asJava, HttpStatus.OK)
    case Failure(e) => new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @GetMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getScript(@PathVariable id: String): ResponseEntity[AnyRef] =
    service.getScript(environment.getProperty(spipesLocation) + "/contexts", id) match {
      case Success(None) => new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
      case Success(Some(context)) => new ResponseEntity(context, HttpStatus.OK)
      case _ => new ResponseEntity("Script with ID " + id + " is not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@PathVariable id: String): ResponseEntity[Any] = {
    service.getModuleTypes(id) match {
      case Success(types) if types.nonEmpty =>
        new ResponseEntity(types.toList.sortBy(_.getLabel()).asJava, HttpStatus.OK)
      case _ =>
        new ResponseEntity("No module types found for the script " + id, HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{id}/modules"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModules(@PathVariable id: String): ResponseEntity[Any] = {
    service.getModules(id) match {
      case Success(Seq()) =>
        new ResponseEntity("Nothing found", HttpStatus.NOT_FOUND)
      case Success(modules) =>
        new ResponseEntity(modules.toSet.asJava, HttpStatus.OK)
      case Failure(e: FileNotFoundException) =>
        new ResponseEntity("Script " + id + " not found", HttpStatus.NOT_FOUND)
      case Failure(e) =>
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}