package cz.cvut.kbss.spipes.service

import java.net.URI
import java.util.Collections

import cz.cvut.kbss.spipes.model.dto.FunctionDTO
import cz.cvut.kbss.spipes.model.dto.filetree.FunctionsDTO
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.ConfigParam._
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.PropertySource
import cz.cvut.sforms.model.Question
import org.apache.jena.rdf.model.{ModelFactory, ResourceFactory}
import org.apache.jena.vocabulary.{RDF, RDFS}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import scala.collection.JavaConverters._
import scala.util.{Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.04.2018.
  */
@Service
class FunctionService extends PropertySource {

  @Autowired
  private var dao: ScriptDao = _

  @Autowired
  private var helper: OntologyHelper = _

  @Autowired
  private var t: RestTemplate = _

  def listFunctions: Iterable[FunctionsDTO] =
    dao.getScripts.map(
      _.map(s =>
        dao.getFunctionStatements(ModelFactory.createDefaultModel().read(s.getAbsolutePath()))
          .map(_.toList().asScala.map(st => {
            val s = st.getSubject()
            new FunctionDTO(
              s.getURI(),
              s.getLocalName(),
              if (s.listProperties(RDFS.comment).hasNext())
                Collections.singleton(s.listProperties(RDFS.comment).next().getString())
              else
                null
            )
          })) match {
          case Success(seq) if seq.nonEmpty => Some(new FunctionsDTO(s.getAbsolutePath(), seq.sortBy(_.getFunctionLocalName()).asJava))
          case _ => None
        }
      )
        .filter(_.nonEmpty).map(_.get)

    )
      .getOrElse(Set())

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
