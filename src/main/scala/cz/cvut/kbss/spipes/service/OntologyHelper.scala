package cz.cvut.kbss.spipes.service

import java.io.{File, FileInputStream}

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.Exceptions._
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import cz.cvut.sforms.Vocabulary
import org.apache.jena.atlas.web.HttpException
import org.apache.jena.ontology.{OntDocumentManager, OntModel, OntModelSpec}
import org.apache.jena.rdf.model.impl.StatementImpl
import org.apache.jena.rdf.model.{Model, ModelFactory, ResourceFactory}
import org.apache.jena.util.FileUtils
import org.apache.jena.vocabulary.{OWL, RDF}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
  */
@Service
class OntologyHelper extends PropertySource with Logger[ScriptService] with ResourceManager {

  @Autowired
  private var scriptDao: ScriptDao = _

  private def getOntologyUri(f: File): Option[String] = {
    log.info(s"""Looking for an ontology in file ${f.getName()}""")
    cleanly(new FileInputStream(f))(_.close())(is => {
      val model = ModelFactory.createDefaultModel()
      val st = model.read(is, null, FileUtils.langTurtle).listStatements(null, RDF.`type`, OWL.Ontology).toList().asScala
      st.map(_.getSubject().getURI())
    }) match {
      case Success(Seq(v)) => Some(v)
      case Success(s) if s.nonEmpty =>
        log.warn(
          s"""Multiple ontologies defined in $f:\n
             |$s
           """.stripMargin)
        s.headOption
      case Success(Seq()) =>
        log.warn(f"""No ontology found in $f""")
        None
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e)
        None
    }
  }

  private def collectOntologyUris(files: Set[File]): Map[String, File] = {
    files.map(f => getOntologyUri(f) -> f).filter(_._1.nonEmpty).map(p => p._1.get -> p._2)
      .foldLeft(Map[String, File]())((acc, p) => {
        if (acc.contains(p._1)) {
          log.warn(f"""Duplicated ontology URI ${p._1} in file ${p._2}. Using the the one in ${acc(p._1)}.""")
          acc
        }
        else
          acc + p
      })
  }

  def getFile(ontologyUri: String): Option[File] = scriptDao.getScriptsWithImports
    .flatMap(s => collectOntologyUris(s.flatMap(_._2)).get(ontologyUri))

  def createOntModel(file: File): Try[OntModel] = scriptDao.getScripts.map(_ -> getOntologyUri(file)) match {
    case None => Failure(new ScriptsNotFoundException)
    case Some((_, None)) => Failure(new OntologyNotFoundException(file))
    case Some((f, Some(o))) if f.nonEmpty =>
      val model = getOntDocumentManager(f).getOntology(o, OntModelSpec.OWL_MEM)
      model.loadImports()
      Success(model)
    case _ => Failure(new ScriptsNotFoundException)
  }

  def getFileDefiningSubject(uri: String): File => Try[String] =
    getFileForStatementFilter(_.containsResource(ResourceFactory.createResource(uri)))

  def getFileDefiningTriple(s: String, p: String, o: String): File => Try[String] =
    getFileForStatementFilter(m => {
      m.contains(new StatementImpl(
        ResourceFactory.createResource(s),
        ResourceFactory.createProperty(p),
        m.getResource(o)
      ))
    })

  private def getFileForStatementFilter(f: Model => Boolean) = (scriptPath: File) =>
    createOntModel(scriptPath).flatMap(m => {
      extractOntology(m, None)(f) match {
        case Some(model) =>
          Success(getFile(model.listStatements(null, RDF.`type`, ResourceFactory.createResource(Vocabulary.s_c_Ontology)).nextStatement().getSubject.getURI()).get.getAbsolutePath())
        case None => Failure(new OntologyNotFoundException(scriptPath))
      }
    })

  def getOntologyURI(m: Model): String = m.listStatements(null, RDF.`type`, OWL.Ontology).next().getSubject().getURI()

  private def getOntDocumentManager(scripts: Set[File]) = {
    val docManager = OntDocumentManager.getInstance()
    docManager.clearCache()
    docManager.setReadFailureHandler((s: String, _: Model, e: Exception) => e match {
      case ex: HttpException if ex.getResponseCode() == 404 =>
        log.warn(f"""Imported ontology $s not found""")
      case ex =>
        log.error(f"""Error processing ontology $s: ${ex.getLocalizedMessage()}""", ex)
    })

    val m = collectOntologyUris(scripts)
    m.foreach(p => docManager.addAltEntry(p._1, p._2.getAbsolutePath()))
    docManager
  }

  private def extractOntology(m: Model, res: Option[Model]): (Model => Boolean) => Option[Model] =
    (f: Model => Boolean) =>
      if (!f(m))
        None
      else m match {
        case model: OntModel if model.listSubModels(true).hasNext() && model.listSubModels(true).filterKeep(f(_)).hasNext =>
          extractOntology(model.listSubModels(true).filterKeep(f(_)).next(), res)(f)
        case model => Some(model)
      }
}
