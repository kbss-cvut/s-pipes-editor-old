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
@RequestMapping(path = Array("/sempipes/"))
@PropertySource(Array("classpath:config.properties"))
class SempipesController {

  @Autowired
  private var environment: Environment = _

  @Autowired
  private var service: SempipesService = _

  private var SempipesLocation = "https://kbss.felk.cvut.cz/sempipes-sped/"

  @GetMapping(path = Array("/contexts/{uri}/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@PathVariable uri: String): ResponseEntity[java.util.Set[ModuleType]] = {
    service.getModuleTypes(SempipesLocation + "contexts/" + uri) match {
      case Some(types) if types.nonEmpty =>
        new ResponseEntity(types.toSet.asJava, HttpStatus.OK)
      case _ =>
        new ResponseEntity(Set[ModuleType]().asJava, HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(path = Array("/contexts/{uri}/modules"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModules(@PathVariable uri: String): ResponseEntity[java.util.Set[Module]] = {
    service.getModules(SempipesLocation + "contexts/" + uri) match {
      case Some(modules) if modules.nonEmpty =>
        new ResponseEntity(modules.toSet.asJava, HttpStatus.OK)
      case _ =>
        new ResponseEntity(Set[Module]().asJava, HttpStatus.NOT_FOUND)
    }
  }
}
