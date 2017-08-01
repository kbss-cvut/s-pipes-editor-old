package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.jopa.model.annotations.OWLClass
import cz.cvut.kbss.spipes.model.AbstractEntity
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
  * Created by yan on 2/10/17.
  */
abstract class AbstractDao[T <: AbstractEntity] {

  protected val LOG: Logger = LoggerFactory.getLogger(getClass())

  @Autowired
  protected var emf: EntityManagerFactory = _

  def get(uri: URI)(implicit tag: ClassTag[T]): Try[Option[T]] = {
    val em = emf.createEntityManager()
    Try(
      em.find(tag.runtimeClass, uri)
    ) match {
      case Success(null) =>
        em.close()
        Success(None)
      case Success(v: T) =>
        em.close()
        Success(Some(v))
      case Failure(e) =>
        em.close()
        Failure(e)
    }
  }

  def findAll(implicit tag: ClassTag[T]): Try[Traversable[T]] = {
    val em = emf.createEntityManager()
    Try {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", tag.runtimeClass)
        .setParameter("type", URI.create(tag.runtimeClass.getAnnotation(classOf[OWLClass]).iri()))
      query.getResultList()
    }
    match {
      case Success(es: JList[T]) =>
        em.close()
        Success(es.asScala.asInstanceOf[Traversable[T]])
      case Failure(x) =>
        em.close()
        Failure(x)
    }
  }

  def save(e: T): Try[T] =
    Try {
      val em = emf.createEntityManager()
      em.getTransaction().begin()
      em.persist(e)
      em.getTransaction().commit()
      em.close()
      e
    }

  def delete(uri: URI)(implicit tag: ClassTag[T]): Try[URI] = {

    get(uri) match {
      case Success(Some(e)) =>
        val em = emf.createEntityManager()
        em.getTransaction().begin()
        em.remove(e)
        em.getTransaction().commit()
        em.close()
        Success(uri)
      case Success(None) =>
        Failure(new IllegalArgumentException("Entity with URI " + uri + " not found and can not be deleted"))
      case Failure(e) => Failure(e)
    }
  }
}