package cz.cvut.kbss.spipes.service

import java.net.URI
import java.util
import java.util.UUID

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.klay.{Child, KGraph, Edge => KEdge}
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.{EdgeDao, NodeDao, ViewDao}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ViewService {

  @Autowired
  private var viewDao: ViewDao = _

  @Autowired
  private var nodeDao: NodeDao = _

  @Autowired
  private var edgeDao: EdgeDao = _

  @Autowired
  private var spipesService: SpipesService = _

  def getView(id: String): Try[Option[View]] =
    viewDao.get(URI.create(Vocabulary.s_c_view + "/" + id))

  def getAllViews: Try[Traversable[View]] =
    viewDao.findAll

  def getAllNodes: Try[Traversable[Node]] =
    nodeDao.findAll

  def getAllEdges: Try[Traversable[Edge]] =
    edgeDao.findAll

  def addView(g: View): Try[View] =
    viewDao.save(g)

  def updateView(id: String, g: View): Try[View] =
    viewDao.updateView(URI.create(Vocabulary.s_c_view + "/" + id), g)

  def deleteView(id: String): Try[URI] =
    viewDao.delete(URI.create(Vocabulary.s_c_view + "/" + id))

  def getViewNodes(id: String): Try[Option[Traversable[Node]]] =
    viewDao.getViewNodes(URI.create(Vocabulary.s_c_view + "/" + id))

  def getViewEdges(id: String): Try[Option[Traversable[Edge]]] =
    viewDao.getViewEdges(URI.create(Vocabulary.s_c_view + "/" + id))

  def getEdge(id: String): Try[Option[Edge]] =
    edgeDao.get(URI.create(Vocabulary.s_c_edge + "/" + id))

  def getNode(id: String): Try[Option[Node]] =
    nodeDao.get(URI.create(Vocabulary.s_c_node + "/" + id))

  def createViewFromSpipes(id: String): Try[View] = {
    spipesService.getModules(id).map(modules => {
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
      val view = new View("Label", nodes.toSet.asJava, edges.toSet.asJava)
      viewDao.save(view)
      view
    }
    )
  }

  def createJsonFromSpipes(id: String): Try[KGraph] =
    createViewFromSpipes(id).map(v => {
      val g = new KGraph(v.getLabel(),
        v.getNodes().asScala
          .map(n => new Child(
            if (n.getLabel() == null)
              "null"
            else n.getLabel(),
            100, 100,
            n.getNodeTypes().asScala.toList.head
          )).asJava,
        v.getEdges().asScala
          .map(e => new KEdge(UUID.randomUUID().toString,
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
    })
}