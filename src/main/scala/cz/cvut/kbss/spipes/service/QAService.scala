package cz.cvut.kbss.spipes.service

import java.io.FileOutputStream

import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Implicits.configParamValue
import cz.cvut.kbss.spipes.{Logger, PropertySource, ResourceManager}
import cz.cvut.sempipes.transform.{Transformer, TransformerImpl}
import cz.cvut.sforms.model.Question
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.springframework.stereotype.Service

import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService extends PropertySource with Logger[QAService] with ResourceManager {

  private val transformer: Transformer = new TransformerImpl()

  def generateForm(script: String, moduleUri: String, moduleTypeUri: String): Try[Question] = {
    log.info("Generating form for script " + script + ", module " + moduleUri + ", moduleType " + moduleTypeUri)
    Try {
      val model = ModelFactory.createDefaultModel()
      model.read(getProperty(SCRIPTS_LOCATION) + "/" + script)
      transformer.script2Form(
        model,
        model.getResource(moduleUri),
        model.getResource(moduleTypeUri)
      )
    }
  }

  def mergeForm(script: String, rootQuestion: Question, moduleType: String): Try[Model] = {
    log.info("Merging form for script " + script)
    val fileName = getProperty(SCRIPTS_LOCATION) + "/" + script
    val model = ModelFactory.createDefaultModel()
    model.read(fileName)
    cleanly(new FileOutputStream(fileName))(_.close())(os => {
      val res = transformer.form2Script(model, rootQuestion, moduleType)
      res.write(os, "TTL")
      res
    })
  }
}