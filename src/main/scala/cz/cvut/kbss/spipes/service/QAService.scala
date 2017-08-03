package cz.cvut.kbss.spipes.service

import java.net.URI

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.view.Node
import cz.cvut.kbss.spipes.persistence.dao.QADao
import cz.cvut.kbss.spipes.rest.dto.RawJson
import cz.cvut.kbss.spipes.util.ConfigParam._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import scala.io.Source
import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService {

  @Autowired
  private var dao: QADao = _

  @Autowired
  private var restTemplate: RestTemplate = _

  @Autowired
  private var environment: Environment = _

  private val scriptsLocation = SCRIPTS_LOCATION.value

  def getNodeById(id: String): Try[Option[Node]] =
    dao.get(URI.create(Vocabulary.s_c_node + "/" + id))

  def generateForm(uri: String): Try[RawJson] =
    Try(
      RawJson(
        Source.fromFile(
          environment.getProperty(scriptsLocation) + "/fss-form.jsonld")
          .mkString)
    )
}