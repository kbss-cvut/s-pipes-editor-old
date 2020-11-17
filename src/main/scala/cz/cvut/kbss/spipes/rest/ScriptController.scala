package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.dto.{DependencyDTO, ModuleDTO, ScriptDTO}
import cz.cvut.kbss.spipes.service.ScriptService
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 05.01.17.
  */
@RestController
@RequestMapping(path = Array("/scripts"))
class ScriptController extends Logger[ScriptController] {

  @Autowired
  private var service: ScriptService = _

  @PostMapping(path = Array("/functions"), produces = Array(JsonLd.MEDIA_TYPE))
  def getFunctions(@RequestBody dto: ScriptDTO): ResponseEntity[Any] = {
    val script = dto.getAbsolutePath()
    log.info("Looking for functions of script " + script)
    service.getFunctions(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info(f"""No functions found for script $script""")
        new ResponseEntity(f"""No functions found for script $script""", HttpStatus.NOT_FOUND)
      case Right(Some(types)) =>
        log.info("Found functions for script " + script)
        log.trace(types)
        new ResponseEntity(types.toList.sortBy(_.getFunctionLocalName()).asJava, HttpStatus.OK)
    }
  }

  @PostMapping(path = Array("/moduleTypes"), produces = Array(JsonLd.MEDIA_TYPE))
  def getModuleTypes(@RequestBody dto: ScriptDTO): ResponseEntity[Any] = {
    val script = dto.getAbsolutePath()
    log.info("Looking for module types of script " + script)
    service.getModuleTypes(script) match {
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
      case Right(None) =>
        log.info(f"""No module types found for script $script""")
        new ResponseEntity(f"""No module types found for script $script""", HttpStatus.NOT_FOUND)
      case Right(Some(types)) =>
        log.info("Found module types for script " + script)
        log.trace(types)
        new ResponseEntity(types.toList.sortBy(_.getLabel()).asJava, HttpStatus.OK)
    }
  }

  @GetMapping
  def getScripts: ResponseEntity[_] = {
    log.info("Looking for any scripts")
    new ResponseEntity(service.getScripts, HttpStatus.OK)
  }

  @PostMapping(path = Array("/modules/dependency"))
  def createDependency(@RequestBody dto: DependencyDTO): ResponseEntity[Any] = {
    val script = dto.getAbsolutePath()
    log.info(f"""Creating ${dto.getModuleUri()}'s dependency on ${dto.getTargetModuleUri()} in script $script""")
    service.createDependency(script, dto.getModuleUri(), dto.getTargetModuleUri()) match {
      case Success(_) =>
        new ResponseEntity[Any](HttpStatus.CREATED)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity[Any](e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  @PostMapping(path = Array("/modules/dependencies/delete"))
  def deleteDependency(@RequestBody dto: DependencyDTO): ResponseEntity[Any] = {
    val script = dto.getAbsolutePath()
    log.info(f"""Deleting ${dto.getModuleUri()}'s dependency on ${dto.getTargetModuleUri()} in script $script""")
    service.deleteDependency(script, dto.getModuleUri(), dto.getTargetModuleUri()) match {
      case Success(_) =>
        new ResponseEntity[Any](HttpStatus.OK)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity[Any](e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  @PostMapping(path = Array("/modules/delete"))
  def deleteModule(@RequestBody dto: ModuleDTO): ResponseEntity[Any] = {
    val scriptPath = dto.getAbsolutePath()
    val module = dto.getModuleUri()
    log.info("Deleting module " + module + " from " + scriptPath)
    service.deleteModule(scriptPath, module) match {
      case Success(_) =>
        log.info("Module " + module + " succesfully deleted from " + scriptPath)
        new ResponseEntity[Any](HttpStatus.NO_CONTENT)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity[Any](e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}
