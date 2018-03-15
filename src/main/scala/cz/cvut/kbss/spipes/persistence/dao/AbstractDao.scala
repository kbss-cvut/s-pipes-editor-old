package cz.cvut.kbss.spipes.persistence.dao

import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.jopa.model.annotations.OWLClass
import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.util.Implicits._
import cz.cvut.kbss.spipes.util.{Logger, ResourceManager}
import org.springframework.beans.factory.annotation.Autowired

import scala.reflect.ClassTag
import scala.util.Try

/**
  * Created by yan on 2/10/17.
  */
abstract class AbstractDao[T <: AbstractEntity] extends Logger[AbstractDao[T]] with ResourceManager {

  @Autowired
  protected var emf: EntityManagerFactory = _

  implicit protected val resultClass: Class[T]

  def get(uri: URI)(implicit resultClass: Class[T]): Try[T] = {
    log.info("Fetching " + resultClass.getSimpleName() + " with URI " + uri)
    cleanly(emf.createEntityManager())(_.close())(em => em.find(resultClass, uri))
  }

  def findAll(implicit tag: ClassTag[T]): Try[JList[T]] = {
    log.info("Fetching all " + tag.runtimeClass.getSimpleName + "s")
    cleanly(emf.createEntityManager())(_.close())(em => {
      val query = em.createNativeQuery("select ?s where { ?s a ?type }", tag.runtimeClass)
        .setParameter("type", URI.create(tag.runtimeClass.getAnnotation(classOf[OWLClass]).iri()))
      query.getResultList().asInstanceOf[JList[T]]
    })
  }

  def save(e: T)(implicit tag: ClassTag[T]): Try[Unit] = {
    log.info("Saving " + tag.runtimeClass.getSimpleName() + ": " + (e + ""))
    cleanly(emf.createEntityManager())(_.close())(em => {
      em.getTransaction().begin()
      em.persist(e)
      em.getTransaction().commit()
    })
  }

  def delete(e: T)(implicit tag: ClassTag[T]): Try[Unit] = {
    log.info("Deleting " + tag.runtimeClass.getSimpleName() + ": " + (e + ""))
    cleanly(emf.createEntityManager())(_.close())(em => {
      em.getTransaction().begin()
      em.remove(e)
      em.getTransaction().commit()
    })
  }
}