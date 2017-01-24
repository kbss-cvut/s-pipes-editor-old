package cz.cvut.kbss.sempipes.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.Node
import cz.cvut.kbss.sempipes.persistence.dao.NodeDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod}
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

  def getNodeById(uri: String): Option[Node] =
    dao.get(URI.create(uri))

  def generateForm(uri: String): String =
    restTemplate.exchange("https://kbss.felk.cvut.cz/sempipes-sped/service?_pId=generate-fss-form",
      HttpMethod.GET,
      new HttpEntity[AnyRef](null, new HttpHeaders()),
      classOf[String]).getBody()
}