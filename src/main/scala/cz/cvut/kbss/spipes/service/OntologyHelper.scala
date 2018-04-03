package cz.cvut.kbss.spipes.service

import java.io.{File, FileInputStream, FileNotFoundException}

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.util.{Logger, PropertySource, ResourceManager}
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.vocabulary.{OWL, RDF}
import org.eclipse.rdf4j.rio.RDFFormat
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
      val st = model.read(is, null, "TTL").listStatements(null, RDF.`type`, OWL.Ontology).toList().asScala
      st.map(_.getSubject().getURI())
    }) match {
      case Success(Seq(v)) => Some(v)
      case Success(v) if v.nonEmpty => // Fixme Delete this abomination once quality scripts are available
        log.warn("The scriptPath contains more than one ontology. Taking only the first one")
        v.headOption
      case Failure(e) =>
        log.warn(e.getLocalizedMessage(), e)
        None
    }
  }

  private def collectOntologyUris(files: Set[File]): Map[String, File] =
    files.map(f => getOntologyUri(f) -> f).filter(_._1.nonEmpty).map(p => p._1.get -> p._2).toMap

  def getFile(ontologyUri: String): Option[File] = scriptDao.getScripts(false).map(collectOntologyUris).flatMap(_.get(ontologyUri))

  def createModel(file: File): Try[Model] =
    if (file.exists())
      cleanly(new FileInputStream(file))(_.close())(is =>
        ModelFactory.createDefaultModel().read(is, null, RDFFormat.TURTLE.getDefaultFileExtension()))
    else
      Failure(new FileNotFoundException(file + " does not exist"))

  def appendImports(model: Model): Model =
    model.union(
      model.listStatements(null, OWL.imports, null).toList().asScala
        .map(st => getFile(st.getObject().asResource().getURI()))
        .filter(_.nonEmpty)
        .map(f => createModel(f.get))
        .filter(_.isSuccess)
        .map(_.get)
        .foldLeft(model)(_.union(_))
    )

  def getUnionModel(file: File): Try[Model] =
    createModel(file).map(m => appendImports(m))
}
