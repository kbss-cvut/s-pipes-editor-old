package cz.cvut.kbss.sempipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.sempipes.model.sempipes.{Module, ModuleType}
import cz.cvut.kbss.sempipes.service.SempipesService
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
  private var service: SempipesService = _

  private var SempipesLocation = "https://kbss.felk.cvut.cz/sempipes-sped/"

  @GetMapping(produces = Array(JsonLd.MEDIA_TYPE))
  def getScripts = None

  @GetMapping(path = Array("/{id}"), produces = Array(JsonLd.MEDIA_TYPE))
  def getScript(@PathVariable id: String) =
    service.getScript(SempipesLocation + "contexts", id) match {
      case Some(context) => new ResponseEntity(context, HttpStatus.OK)
      case None => new ResponseEntity("Script with ID " + id + " is not found", HttpStatus.NOT_FOUND)
    }

  @GetMapping(path = Array("/{id}/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@PathVariable id: String): ResponseEntity[java.util.Set[ModuleType]] = {
    service.getModuleTypes(SempipesLocation + "contexts/" + id) match {
      case Some(types) if types.nonEmpty =>
        new ResponseEntity(types.toSet.asJava, HttpStatus.OK)
      case _ =>
        new ResponseEntity(Set[ModuleType]().asJava, HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/{id}/modules"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModules(@PathVariable id: String): ResponseEntity[java.util.Set[Module]] = {
    service.getModules(SempipesLocation + "contexts/" + id) match {
      case Some(modules) if modules.nonEmpty =>
        new ResponseEntity(modules.toSet.asJava, HttpStatus.OK)
      case _ =>
        new ResponseEntity(Set[Module]().asJava, HttpStatus.NOT_FOUND)
    }
  }
}