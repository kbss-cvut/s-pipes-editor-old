package cz.cvut.kbss.spipes.service

import java.util

import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.ViewDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ViewService {

  @Autowired
  private var viewDao: ViewDao = _

  @Autowired
  private var spipesService: SpipesService = _

  def newViewFromSpipes(script: String): Either[Throwable, Option[View]] = {
    spipesService.getModules(script) match {
      case Right(Some(modules)) =>
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
        viewDao.save(view)
        Right(Some(view))
      case Right(None) => Right(None)
      case Left(e) => Left(e)
    }
  }
}