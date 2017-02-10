package cz.cvut.kbss.sempipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao extends AbstractDao[Graph] {

  def getNode(uri: URI): Option[Node] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Node], uri) match {
        case n: Node => Some(n)
        case null => None
      }
    }
    catch {
      case e: Exception =>
        LOG.error("Exception in " + getClass().getSimpleName(), e)
        None
    }
    finally {
      em.close()
    }
  }

  def getEdge(uri: URI): Option[Edge] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Edge], uri) match {
        case e: Edge => Some(e)
        case null => None
      }
    }
    catch {
      case e: Exception =>
        LOG.error("Exception in " + getClass().getSimpleName(), e)
        None
    }
    finally {
      em.close()
    }
  }

  def getGraph(uri: URI): Option[Graph] = {
    get(uri)
  }

  def getAllGraphs(): Option[Traversable[Graph]] = {
    val em = emf.createEntityManager()
    try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Graph])
        .setParameter("type", URI.create(Vocabulary.s_c_graph))
      query.getResultList() match {
        case nonEmpty: java.util.List[Graph] if !nonEmpty.isEmpty =>
          Some(nonEmpty.asScala)
        case empty: java.util.List[Graph] if empty.isEmpty =>
          None
      }
    }
    finally {
      em.close()
    }
  }

  def getNodes(uri: URI): Option[Traversable[Node]] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Graph], uri) match {
        case g: Graph if g.getNodes() != null && !g.getNodes().isEmpty() =>
          Some(g.getNodes().asScala)
        case _ => None
      }
    }
    catch {
      case e: Exception =>
        LOG.error("Exception in " + getClass().getSimpleName(), e)
        None
    }
    finally {
      em.close()
    }
  }

  def getEdges(uri: URI): Option[Traversable[Edge]] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Graph], uri) match {
        case g: Graph if g.getEdges() != null && !g.getEdges().isEmpty() =>
          Some(g.getEdges().asScala)
        case _ => None
      }
    }
    catch {
      case e: Exception =>
        LOG.error("Exception in " + getClass().getSimpleName(), e)
        None
    }
    finally {
      em.close()
    }
  }
}