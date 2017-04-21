package cz.cvut.kbss.spipes.service

import java.net.URI

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.dto.{Child, KGraph, Edge => KEdge}
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
  private var dao: ViewDao = _

  @Autowired
  private var spipesService: SpipesService = _

  def getView(id: String): Option[View] =
    dao.get(URI.create(Vocabulary.s_c_view + "/" + id))

  def getAllViews(): Option[Traversable[View]] =
    dao.getAllViews()

  def getAllNodes(): Option[Traversable[Node]] =
    dao.getAllNodes()

  def getAllEdges(): Option[Traversable[Edge]] =
    dao.getAllEdges()

  def addView(g: View): Option[View] =
    dao.add(g)

  def updateView(id: String, g: View): Option[View] =
    dao.update(URI.create(Vocabulary.s_c_view + "/" + id), g)

  def deleteView(id: String): Option[URI] =
    dao.delete(URI.create(Vocabulary.s_c_view + "/" + id))

  def getViewNodes(id: String): Option[Traversable[Node]] =
    dao.getViewNodes(URI.create(Vocabulary.s_c_view + "/" + id))

  def getViewEdges(id: String): Option[Traversable[Edge]] =
    dao.getViewEdges(URI.create(Vocabulary.s_c_view + "/" + id))

  def getEdge(id: String): Option[Edge] =
    dao.getEdge(URI.create(Vocabulary.s_c_edge + "/" + id))

  def getNode(id: String): Option[Node] =
    dao.getNode(URI.create(Vocabulary.s_c_node + "/" + id))

  def createViewFromSpipes(id: String): Option[View] = {
    spipesService.getModules(id) match {
      case Some(modules) =>
        val nodes = modules.map(m => new Node(m.getLabel(), 0, 0, null, null, null))
        val edges = modules
          .filter(_.getNext() != null)
          .flatMap(m => m.getNext().asScala
            .map(n => new Edge(nodes.find(_.getLabel == m.getLabel).get, nodes.find(_.getLabel == n.getLabel).get)))
        val view = new View("Label", nodes.toSet.asJava, edges.toSet.asJava)
        dao.add(view)
        Some(view)
      case None =>
        None
    }
  }

  def createJsonFromSpipes(id: String) =
    createViewFromSpipes(id) match {
      case Some(v) =>
        val g = new KGraph(v.getLabel(),
          v.getNodes().asScala
            .map(n => new Child(
              if (n.getLabel() == null)
                "null"
              else n.getLabel(),
              100, 100)).asJava,
          v.getEdges().asScala
            .map(e => new KEdge("next",
              if (e.getSourceNode().getLabel() == null)
                "null"
              else
                e.getSourceNode().getLabel(),
              if (e.getDestinationNode().getLabel() == null)
                "null"
              else
                e.getDestinationNode().getLabel()))
            .asJava)
        g
    }
}