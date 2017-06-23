package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.jopa.model.annotations.OWLClass
import cz.cvut.kbss.spipes.model.AbstractEntity
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.util.{Success, Try}

/**
  * Created by yan on 2/10/17.
  */
abstract class AbstractDao[T <: AbstractEntity] {

  protected val LOG: Logger = LoggerFactory.getLogger(getClass())

  @Autowired
  protected var emf: EntityManagerFactory = _

  def get(uri: URI)(implicit tag: ClassTag[T]): Option[T] = {
    val em = emf.createEntityManager()
    Try(
      em.find(tag.runtimeClass, uri)
    ) match {
      case Success(v: T) if v != null =>
        em.close()
        Some(v)
      case _ =>
        em.close()
        None
    }
  }

  def findAll(implicit tag: ClassTag[T]): Option[Traversable[T]] = {
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", tag.runtimeClass)
        .setParameter("type", URI.create(tag.runtimeClass.getAnnotation(classOf[OWLClass]).iri()))
      query.getResultList()
    }
    match {
      case Success(es: JList[T]) =>
        em.close()
        Some(es.asScala.asInstanceOf[Traversable[T]])
      case _ =>
        em.close()
        None
    }
  }

  def save(e: T): Option[T] = {
    val em = emf.createEntityManager()
    Try {
      em.getTransaction().begin()
      em.persist(e)
      em.getTransaction().commit()
      e
    } match {
      case Success(v: T) if v != null =>
        em.close()
        Some(e)
      case _ =>
        em.close()
        None

    }
  }

  def delete(uri: URI)(implicit tag: ClassTag[T]): Option[URI] = {
    val em = emf.createEntityManager()
    Try {
          em.getTransaction().begin()
      em.remove(get(uri))
          em.getTransaction().commit()
      uri
    } match {
      case Success(uri: URI) if uri != null =>
        em.close()
        Some(uri)
      case _ =>
        em.close()
        None
    }
  }
}