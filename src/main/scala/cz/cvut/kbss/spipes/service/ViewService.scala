package cz.cvut.kbss.spipes.service

import java.util

import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.ViewDao
import cz.cvut.kbss.spipes.util.Implicits._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ViewService {

  private final val log = LoggerFactory.getLogger(classOf[ViewService])

  @Autowired
  private var viewDao: ViewDao = _

  @Autowired
  private var spipesService: ScriptService = _

  def newViewFromSpipes(script: String): Either[Throwable, Option[View]] = {
    log.info("Creating a view for script " + script)
    spipesService.getModules(script) match {
      case Right(Some(modules)) =>
        log.info("Modules for script " + script + " found")
        log.trace(modules)
        val nodes = modules.map(m => new Node(
          m.getUri(),
          m.getId(),
          m.getLabel(),
          0d,
          0d,
          m.getTypes(),
          new util.HashSet[String](),
          new util.HashSet[String]()))
        val edges = modules
          .filter(_.getNext() != null)
          .flatMap(m => m.getNext().asScala
            .filter(_ != null)
            .map(n => new Edge(
              nodes.find(_.getUri == m.getUri).get,
              nodes.find(_.getUri == n.getUri()).get)))
        val view = new View(script, nodes.toSet.asJava, edges.toSet.asJava)
        log.info("Created view for script " + script)
        log.trace(view)
        viewDao.save(view)
        Right(Some(view))
      case Right(None) =>
        log.info("No modules found for script " + script)
        Right(None)
      case Left(e) =>
        log.error(e.getLocalizedMessage(), e)
        Left(e)
    }
  }
}