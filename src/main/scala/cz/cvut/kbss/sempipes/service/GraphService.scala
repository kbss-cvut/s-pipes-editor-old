package cz.cvut.kbss.sempipes.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class GraphService {

  @Autowired
  var dao: GraphDao = _

  def getGraphByUri(uri: URI): Option[Graph] =
    dao.get(uri)

  def getAllGraphs(): Option[Traversable[Graph]] =
    dao.getAll()

  def updateGraph(uri: URI, g: Graph): Option[Graph] =
    dao.update(uri, g)

  def delete(uri: URI): Option[URI] =
    dao.delete(uri)

  def getGraphNodes(uri: URI): Option[Traversable[Node]] =
    dao.get(uri) match {
      case Some(g) => Some(g.getNodes.asScala)
      case None => None
    }

  def getGraphEdges(uri: URI): Option[Traversable[Edge]] =
    dao.get(uri) match {
      case Some(g) => Some(g.getEdges.asScala)
      case None => None
    }
}
