package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.spipes.model.Vocabulary
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.util.{Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class ViewDao extends AbstractDao[View] {

  def getAllNodes: Option[Traversable[Node]] = {
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Node])
        .setParameter("type", URI.create(Vocabulary.s_c_node))
      query.getResultList()
    }
    match {
      case Success(ns: JList[Node]) =>
        em.close()
        Some(ns.asScala)
      case _ =>
        em.close()
        None
    }
  }

  def getAllEdges: Option[Traversable[Edge]] = {
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Edge])
        .setParameter("type", URI.create(Vocabulary.s_c_edge))
      query.getResultList()
    }
    match {
      case Success(es: JList[Edge]) =>
        em.close()
        Some(es.asScala)
      case _ =>
        em.close()
        None
    }
  }

  def getAllViews: Option[Traversable[View]] = {
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", classOf[View])
        .setParameter("type", URI.create(Vocabulary.s_c_view))
      query.getResultList()
    }
    match {
      case Success(vs: JList[View]) =>
        em.close()
        Some(vs.asScala)
      case _ =>
        em.close()
        None
    }
  }

  def getViewNodes(uri: URI): Option[Traversable[Node]] =
    get(uri).map(_.getNodes.asScala)

  def getViewEdges(uri: URI): Option[Traversable[Edge]] =
    get(uri).map(_.getEdges.asScala)

  def updateView(uri: URI, other: View)(implicit tag: ClassTag[View]): Option[View] = {
    val em = emf.createEntityManager()
    Try {
      em.getTransaction().begin()
      get(uri).map(v => {
        v.setLabel(other.getLabel)
        v.setNodes(other.getNodes)
        v.setEdges(other.getEdges)
        em.merge(v)
        em.getTransaction().commit()
        v
      })
    } match {
      case Success(v) =>
        em.close()
        v
      case _ =>
        em.close()
        None
    }
  }
}