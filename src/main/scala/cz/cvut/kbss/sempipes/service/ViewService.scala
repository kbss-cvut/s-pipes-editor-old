package cz.cvut.kbss.sempipes.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.sempipes.persistence.dao.ViewDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class ViewService {

  @Autowired
  private var dao: ViewDao = _

  @Autowired
  private var sempipesService: SempipesService = _

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


  import scala.collection.JavaConverters._

  def createViewFromSempipes(id: String): Option[View] = {
    sempipesService.getModules(id) match {
      case Some(modules) =>
        val nodes = modules.map(m => new Node(m.getLabel(), 0, 0, Set("").asJava, Set("").asJava, Set("").asJava)).toList
        val edges = Seq(new Edge(nodes.head, nodes.last), new Edge(nodes.last, nodes.head))
        //        dao.get(URI.create(Vocabulary.s_c_view + "/" + id)) match {
        //          case Some(view) =>
        //            view.setNodes(nodes.toSet.asJava)
        //            view.setEdges(edges.toSet.asJava)
        //            dao.update(view.getUri, view)
        //          case None =>
        //            val view = new View("Some mr. view man",
        //              nodes.toSet.asJava,
        //              edges.toSet.asJava)
        //            dao.add(view)
        //        }
        import scala.collection.JavaConverters._
        Some(new View("Label", nodes.toSet.asJava, edges.toSet.asJava))
      case None =>
        None
    }
  }
}