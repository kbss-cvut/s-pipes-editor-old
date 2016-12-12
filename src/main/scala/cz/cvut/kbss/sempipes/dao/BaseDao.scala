package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.12.16.
  */

trait BaseDao[T] {

  @Autowired
  protected var emf: EntityManagerFactory = _

  // Can't be implemented due to Scala's type erasure system
  def get(uri: URI): Option[T]

  def add(e: T): Option[T] = {
    assert(e != null)
    val em = emf.createEntityManager()
    em.getTransaction().begin()
    em.persist(e)
    em.getTransaction().commit()
    Some(e)
  }

  // Can't be implemented due to Scala's type erasure system
  def delete(uri: URI): Option[URI]
}