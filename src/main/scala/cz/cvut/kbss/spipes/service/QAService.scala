package cz.cvut.kbss.spipes.service

import java.net.URI

import cz.cvut.kbss.spipes.exception.SpipesException
import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.view.Node
import cz.cvut.kbss.spipes.persistence.dao.QADao
import cz.cvut.kbss.spipes.rest.dto.RawJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http._
import org.springframework.stereotype.Service
import org.springframework.web.client.{HttpClientErrorException, RestTemplate}

import scala.util.{Failure, Success, Try}

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

  def generateForm(uri: String): Either[Throwable, RawJson] = {
    Try {
      val url = "https://kbss.felk.cvut.cz/sempipes-vfn-dev/service"
      import org.springframework.web.util.UriComponentsBuilder
      val builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("_pId", "generate-fss-form")
        .queryParam("formGenRepositoryUrl", "http://onto.fel.cvut.cz/rdf4j-server/repositories/fss-study-dev-formgen")
        .queryParam("recordGraphId", "http://vfn.cz/ontologies/study-manager/formGen1496673137843")
        .queryParam("repositoryUrl", "http://onto.fel.cvut.cz/rdf4j-server/repositories/fss-study-dev-app")
      val r = restTemplate.exchange(builder.build().encode().toUri,
        HttpMethod.GET,
        new HttpEntity[AnyRef](null, new HttpHeaders()),
        classOf[String])
      r
    }
    match {
      case Success(r: ResponseEntity[String]) if r.getStatusCode() == HttpStatus.OK =>
        Right(RawJson(r.getBody()))
      case Success(r: ResponseEntity[String]) =>
        Left(new SpipesException(r.getStatusCode(), r.getBody()))
      case Failure(e: HttpClientErrorException) =>
        Left(new SpipesException(e.getStatusCode(), e.getLocalizedMessage()))
      case Failure(e) =>
        Left(e)
    }
  }
}