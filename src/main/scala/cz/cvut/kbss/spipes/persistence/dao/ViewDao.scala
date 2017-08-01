package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class ViewDao extends AbstractDao[View] {

  def getViewNodes(uri: URI): Try[Option[Traversable[Node]]] =
    get(uri).map(_.map(_.getNodes.asScala))

  def getViewEdges(uri: URI): Try[Option[Traversable[Edge]]] =
    get(uri).map(_.map(_.getEdges.asScala))

  def updateView(uri: URI, other: View)(implicit tag: ClassTag[View]): Try[View] =
    get(uri) match {
      case Success(Some(v)) =>
        val em = emf.createEntityManager()
        em.getTransaction().begin()
        v.setLabel(other.getLabel)
        v.setNodes(other.getNodes)
        v.setEdges(other.getEdges)
        em.merge(v)
        em.getTransaction().commit()
        em.close()
        Success(v)
      case Success(None) => Failure(new IllegalArgumentException("View with URI " + uri + "not found"))
      case Failure(e) => Failure(e)
    }
}