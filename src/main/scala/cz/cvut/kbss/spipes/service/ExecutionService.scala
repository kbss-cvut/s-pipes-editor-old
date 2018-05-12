package cz.cvut.kbss.spipes.service

import java.net.URI

import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.PropertySource
import cz.cvut.sforms.model.Question
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.vocabulary.RDF
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.04.2018.
  */
@Service
class ExecutionService extends PropertySource {

  @Autowired
  private var t: RestTemplate = _

  def requestExecution(q: Question) = Try {
    val executionId = q.getAnswers().iterator().next().getTextValue()

    def collectLeaves(root: Question, acc: Set[Question]): Set[Question] =
      if (root.getSubQuestions() == null)
        acc + root
      else
        root.getSubQuestions().asScala.flatMap(collectLeaves(_, acc)).toSet

    val e = getProperty(EXECUTION_ENDPOINT)
    val params = (for (q <- collectLeaves(q, Set())) yield
      (if (q.getOrigin().toString() == RDF.uri) "id"
      else q.getLabel()) -> ResourceFactory.createResource(q.getAnswers().iterator().next().getTextValue()).getLocalName())
      .toMap

    val urlWithQuery = prepareUri(e, params)
    executionId -> t.exchange(urlWithQuery, HttpMethod.GET, HttpEntity.EMPTY, classOf[String]).getStatusCode()
  }


  private def prepareUri(remoteUrl: String, queryParams: Map[String, String]) = {
    val sb = new StringBuilder(remoteUrl)
    var containsQueryString = remoteUrl.matches("^.+\\?.+=.+$")
    for ((k, v) <- queryParams) {
      sb.append(if (!containsQueryString) '?'
      else '&')
      sb.append(k).append('=').append(v)
      containsQueryString = true
    }
    URI.create(sb.toString)
  }
}
