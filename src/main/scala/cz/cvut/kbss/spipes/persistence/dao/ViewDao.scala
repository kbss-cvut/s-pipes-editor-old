package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

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