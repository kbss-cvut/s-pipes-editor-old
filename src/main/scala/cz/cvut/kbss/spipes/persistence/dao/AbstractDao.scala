package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.jopa.model.annotations.OWLClass
import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.util.Implicits._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
  * Created by yan on 2/10/17.
  */
abstract class AbstractDao[T <: AbstractEntity] {

  protected final val log = LoggerFactory.getLogger(classOf[AbstractDao[T]])

  @Autowired
  protected var emf: EntityManagerFactory = _

  def get(uri: URI)(implicit tag: ClassTag[T]): Try[T] = {
    log.info("Fetching " + tag.runtimeClass.getSimpleName + " with URI " + uri)
    val em = emf.createEntityManager()
    Try(
      em.find(tag.runtimeClass, uri)
    ) match {
      case t: Try[T] =>
        em.close()
        t
    }
  }

  def findAll(implicit tag: ClassTag[T]): Try[JList[T]] = {
    log.info("Fetching all " + tag.runtimeClass.getSimpleName + "s")
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", tag.runtimeClass)
        .setParameter("type", URI.create(tag.runtimeClass.getAnnotation(classOf[OWLClass]).iri()))
      query.getResultList()
    }
    match {
      case Success(l) if l.isInstanceOf[JList[T]] =>
        em.close()
        Success(l.asInstanceOf[JList[T]])
      case Failure(e) =>
        em.close()
        Failure(e)
    }
  }

  def save(e: T)(implicit tag: ClassTag[T]): Try[Unit] = {
    log.info("Saving " + tag.runtimeClass.getSimpleName() + ": " + (e + ""))
    val em = emf.createEntityManager()
    Try {
      em.getTransaction().begin()
      em.persist(e)
      em.getTransaction().commit()
    } match {
      case t: Try[Unit] =>
        em.close()
        t
    }
  }

  def delete(t: T)(implicit tag: ClassTag[T]): Try[Unit] = {
    log.info("Deleting " + tag.runtimeClass.getSimpleName() + ": " + (t + ""))
    val em = emf.createEntityManager()
    Try {
      em.getTransaction().begin()
      em.remove(t)
      em.getTransaction().commit()
    } match {
      case t: Try[Unit] =>
        em.close()
        t
    }
  }
}