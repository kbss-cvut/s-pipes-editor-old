package cz.cvut.kbss.sempipes.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.view.Node
import cz.cvut.kbss.sempipes.persistence.dao.NodeDao
import cz.cvut.kbss.sempipes.rest.dto.RawJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, HttpStatus}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class NodeService {

  @Autowired
  private var dao: NodeDao = _

  @Autowired
  private var restTemplate: RestTemplate = _

  def getNodeById(id: String): Option[Node] =
    dao.get(URI.create(Vocabulary.s_c_node + "/" + id))

  def generateForm(uri: String): Option[RawJson] = {
    val r = restTemplate.exchange("https://kbss.felk.cvut.cz/sempipes-sped/service?_pId=generate-fss-form",
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