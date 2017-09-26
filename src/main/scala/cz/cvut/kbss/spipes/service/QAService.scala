package cz.cvut.kbss.spipes.service

import cz.cvut.kbss.spipes.dto.RawJson
import cz.cvut.kbss.spipes.util.ConfigParam._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import scala.io.Source
import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService {

  @Autowired
  private var environment: Environment = _

  private val formsLocation = FORMS_LOCATION.value

  def generateForm(uri: String): Try[RawJson] =
    Try(
      RawJson(
        Source.fromFile(
          environment.getProperty(formsLocation) + "/new-form.jsonld")
          .mkString)
    )
}