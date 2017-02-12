package cz.cvut.kbss.sempipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.view.{Edge, Node, View}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class ViewDao extends AbstractDao[View] {

  def getAllNodes(): Option[Traversable[Node]] = {
    val em = emf.createEntityManager()
    try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Node])
        .setParameter("type", URI.create(Vocabulary.s_c_node))
      query.getResultList() match {
        case nonEmpty: java.util.List[Node] if !nonEmpty.isEmpty =>
          Some(nonEmpty.asScala)
        case empty: java.util.List[Node] if empty.isEmpty =>
          None
      }
    }
    finally {
      em.close()
    }
  }

  def getAllEdges(): Option[Traversable[Edge]] = {
    val em = emf.createEntityManager()
    try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Edge])
        .setParameter("type", URI.create(Vocabulary.s_c_edge))
      query.getResultList() match {
        case nonEmpty: java.util.List[Edge] if !nonEmpty.isEmpty =>
          Some(nonEmpty.asScala)
        case empty: java.util.List[Edge] if empty.isEmpty =>
          None
      }
    }
    finally {
      em.close()
    }
  }

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

  def getAllViews(): Option[Traversable[View]] = {
    val em = emf.createEntityManager()
    try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[View])
        .setParameter("type", URI.create(Vocabulary.s_c_view))
      query.getResultList() match {
        case nonEmpty: java.util.List[View] if !nonEmpty.isEmpty =>
          Some(nonEmpty.asScala)
        case empty: java.util.List[View] if empty.isEmpty =>
          None
      }
    }
    finally {
      em.close()
    }
  }

  def getViewNodes(uri: URI): Option[Traversable[Node]] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[View], uri) match {
        case g: View if g.getNodes() != null && !g.getNodes().isEmpty() =>
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

  def getViewEdges(uri: URI): Option[Traversable[Edge]] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[View], uri) match {
        case g: View if g.getEdges() != null && !g.getEdges().isEmpty() =>
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