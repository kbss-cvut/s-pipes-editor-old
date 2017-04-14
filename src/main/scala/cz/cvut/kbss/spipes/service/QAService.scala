package cz.cvut.kbss.spipes.service

import java.net.URI

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.view.Node
import cz.cvut.kbss.spipes.persistence.dao.QADao
import cz.cvut.kbss.spipes.rest.dto.RawJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, HttpStatus}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class QAService {

  @Autowired
  private var dao: QADao = _

  @Autowired
  private var restTemplate: RestTemplate = _

  def getNodeById(id: String): Option[Node] =
    dao.get(URI.create(Vocabulary.s_c_node + "/" + id))

  def generateForm(uri: String): Option[RawJson] = {
    val r = restTemplate.exchange("https://kbss.felk.cvut.cz/spipes-sped/service?_pId=generate-fss-form",
      HttpMethod.GET,
      new HttpEntity[AnyRef](null, new HttpHeaders()),
      classOf[String])
    r.getStatusCode() match {
      case HttpStatus.OK =>
        Some(RawJson(r.getBody()))
      case _ =>
        None
    }
  }
}