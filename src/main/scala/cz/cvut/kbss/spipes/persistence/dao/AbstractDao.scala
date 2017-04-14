package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.model.view.View
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.reflect.ClassTag

/**
  * Created by yan on 2/10/17.
  */
abstract class AbstractDao[T <: AbstractEntity] {

  protected val LOG = LoggerFactory.getLogger(getClass())

  @Autowired
  protected var emf: EntityManagerFactory = _

  def get(uri: URI)(implicit tag: ClassTag[T]): Option[T] = {
    val em = emf.createEntityManager()
    try {
      em.find(tag.runtimeClass, uri) match {
        case n: T => Some(n)
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

  def add(e: T): Option[T] = {
    assert(e != null)
    val em = emf.createEntityManager()
    try {
      em.getTransaction().begin()
      em.persist(e)
      em.getTransaction().commit()
      Some(e)
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

  def delete(uri: URI)(implicit tag: ClassTag[T]): Option[URI] = {
    val em = emf.createEntityManager()
    try {
      em.find(tag.runtimeClass, uri) match {
        case n: View =>
          em.getTransaction().begin()
          em.remove(n)
          em.getTransaction().commit()
          Some(uri)
        case null =>
          None
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

  def update(uri: URI, other: View)(implicit tag: ClassTag[T]): Option[View] = {
    assert(other != null)
    val em = emf.createEntityManager()
    try {
      em.getTransaction().begin()
      em.find(tag.runtimeClass, uri) match {
        case g: View =>
          g.setLabel(other.getLabel)
          g.setNodes(other.getNodes)
          g.setEdges(other.getEdges)
          em.merge(g)
          em.getTransaction().commit()
          Some(g)
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
}