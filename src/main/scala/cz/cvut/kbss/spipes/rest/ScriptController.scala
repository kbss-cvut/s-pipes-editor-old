package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.rest.ScriptController._
import cz.cvut.kbss.spipes.service.ScriptService
import cz.cvut.kbss.spipes.util.ConfigParam.SPIPES_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping, RestController}

import scala.collection.JavaConverters.{seqAsJavaListConverter, setAsJavaSetConverter}

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
  private var service: ScriptService = _

  private val spipesLocation = SPIPES_LOCATION.value

  @GetMapping(path = Array("/{script}/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@PathVariable script: String): ResponseEntity[Any] = {
    log.info("Looking for module types of script " + script)
    service.getModuleTypes(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info("No module types found for script " + script)
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(types)) =>
        log.info("Found module types for script " + script)
        log.trace(types)
        new ResponseEntity(types.toList.sortBy(_.getLabel()).asJava, HttpStatus.OK)
    }
  }


  @GetMapping(path = Array("/{script}/modules"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModules(@PathVariable script: String): ResponseEntity[Any] = {
    log.info("Looking for modules types of script " + script)
    service.getModules(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info("No modules found for script " + script)
        new ResponseEntity(HttpStatus.NOT_FOUND)
      case Right(Some(modules)) =>
        log.info("Found modules for script " + script)
        log.trace(modules)
        new ResponseEntity(modules.toSet.asJava, HttpStatus.OK)
    }
  }

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getScripts: ResponseEntity[Any] = {
    log.info("Looking for any scripts")
    service.getScriptNames match {
      case Some(s) =>
        log.info("Scripts found")
        log.trace(s.mkString("[", ",", "]"))
        new ResponseEntity(s.asJava, HttpStatus.OK)
      case None =>
        log.info("No scripts found")
        new ResponseEntity("No scripts found", HttpStatus.NOT_FOUND)
    }
  }
}

object ScriptController {
  private final val log = LoggerFactory.getLogger(classOf[ScriptController])
}