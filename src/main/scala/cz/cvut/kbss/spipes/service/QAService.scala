package cz.cvut.kbss.spipes.service

import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.sempipes.transform.TransformerImpl
import cz.cvut.sforms.model.Question
import org.apache.jena.rdf.model.ModelFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService {

  private final val log = LoggerFactory.getLogger(classOf[QAService])

  @Autowired
  private var environment: Environment = _

  private val formsLocation = FORMS_LOCATION.value
  private val scriptsLocation = SCRIPTS_LOCATION.value

  def generateForm(script: String, moduleUri: String, moduleTypeUri: String): Try[Question] = {
    log.info("Generating form for script " + script + ", module " + moduleUri + ", moduleType " + moduleTypeUri)
    Try {
      val model = ModelFactory.createDefaultModel()
      model.read(environment.getProperty(scriptsLocation) + "/" + script)
      new TransformerImpl().script2Form(
        model,
        model.getResource(moduleUri),
        model.getResource(moduleTypeUri)
      )
    }
  }
}