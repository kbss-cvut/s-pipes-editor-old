package cz.cvut.kbss.spipes.service

import java.io.{File, FileOutputStream}

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import cz.cvut.kbss.spipes.websocket.NotificationController
import cz.cvut.sforms.model.Question
import cz.cvut.spipes.transform.{Transformer, TransformerImpl}
import org.apache.jena.rdf.model.Resource
import org.apache.jena.vocabulary.RDF
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService extends PropertySource with Logger[QAService] with ResourceManager {

  @Autowired
  private var helper: OntologyHelper = _

  private val transformer: Transformer = new TransformerImpl()

  def generateModuleForm(scriptPath: String, moduleUri: String, moduleTypeUri: String): Try[Question] = {
    log.info("Generating form for script " + scriptPath + ", module " + moduleUri + ", moduleType " + moduleTypeUri)
    helper.createOntModel(new File(scriptPath)).map(model => {
      val moduleType = model.listStatements(model.getResource(moduleUri), RDF.`type`, null)
        .filterDrop(_.getObject().asResource().getURI() == Vocabulary.s_c_Modules).nextOptional()
      transformer.script2Form(
        model.getResource(moduleUri),
        moduleType.map[Resource](_.getObject().asResource()).orElse(model.getResource(moduleTypeUri))
      )
    })
  }

  def mergeForm(scriptPath: String, rootQuestion: Question, moduleType: String): Try[_] = {
    log.info("Merging form for script " + scriptPath)
    helper.createOntModel(new File(scriptPath)).flatMap(model => {
      log.info(s"Ont model for $scriptPath created")
      Try(transformer.form2Script(model, rootQuestion, moduleType))
    }).map { res =>
      log.info(s"Created updated ont model for $scriptPath")
      res.asScala.foreach(p => {
        helper.getFile(p._1).map(f =>
          cleanly(new FileOutputStream(f))(_.close())(os => {
            log.info(s"Writing model to file $f")
            p._2.write(os, "TTL")
            NotificationController.notify(scriptPath)
          }
          ))
      })
    }
  }

  def generateFunctionForm(scriptPath: String, functionUri: String): Try[Question] = {
    log.info(s"Generating form for script $scriptPath, function $functionUri")
    helper.createOntModel(new File(scriptPath)).map(model => {
      transformer.functionToForm(model, model.getResource(functionUri))
    })
  }
}