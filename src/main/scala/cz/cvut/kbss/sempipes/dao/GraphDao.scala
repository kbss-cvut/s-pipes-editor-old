package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao extends BaseDao[Graph] {

  override def get(uri: URI): Option[Graph] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Graph], uri) match {
        case g: Graph => Some(g)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }

  def getAll(): Traversable[Graph] =
    List()

  override def delete(uri: URI): Option[URI] = {
    val em = emf.createEntityManager()
    em.getTransaction().begin()
    try {
      em.find(classOf[Graph], uri) match {
        case n: Graph =>
          em.remove(n)
          em.getTransaction().commit()
          Some(uri)
        case null =>
          None
      }
    }
    finally {
      em.close()
    }
  }

  def update(uri: URI, other: Graph): Option[Graph] = {
    assert(other != null)
    val em = emf.createEntityManager()
    try {
      em.getTransaction().begin()
      em.find(classOf[Graph], uri) match {
        case g: Graph =>
          g.setLabel(other.getLabel)
          g.setNodes(other.getNodes)
          g.setEdges(other.getEdges)
          Some(g)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }
}