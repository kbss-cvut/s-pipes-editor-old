package cz.cvut.kbss.spipes.rest

import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.spipes.model.spipes.{NextDTO, QuestionDTO}
import cz.cvut.kbss.spipes.service.ScriptService
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters.{seqAsJavaListConverter, setAsJavaSetConverter}
import scala.util.{Failure, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 05.01.17.
  */
@RestController
@RequestMapping(path = Array("/scripts"))
class ScriptController extends Logger[ScriptController] {

  @Autowired
  private var service: ScriptService = _

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

  @PostMapping(path = Array("/{script}/modules/dependency"))
  def createDependency(
                        @PathVariable script: String,
                        @RequestBody dto: NextDTO
                      ): ResponseEntity[Any] = {
    service.createDependency(script, dto.getSourceUri(), dto.getTargetUri()) match {
      case Success(_) =>
        new ResponseEntity[Any](HttpStatus.CREATED)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity[Any](e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  @PostMapping(path = Array("/{script}/modules/delete"))
  def deleteModule(
                    @PathVariable script: String,
                    @RequestBody dto: QuestionDTO
                  ): ResponseEntity[Any] = {
    val module = dto.getModule()
    log.info("Deleting module " + module + " from " + script)
    service.deleteModule(script, module) match {
      case Success(_) =>
        log.info("Module " + module + " succesfully deleted from " + script)
        new ResponseEntity[Any](HttpStatus.NO_CONTENT)
      case Failure(e: Throwable) =>
        log.error(e.getLocalizedMessage(), e)
        new ResponseEntity[Any](e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}
