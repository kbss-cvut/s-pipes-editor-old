package cz.cvut.kbss.sempipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.sempipes.model.graph.Node
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class NodeDao {

  @Autowired
  private var emf: EntityManagerFactory = _

  def get(uri: URI): Option[Node] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Node], uri) match {
        case n: Node => Some(n)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }

  def add(e: Node): Option[Node] = {
    assert(e != null)
    val em = emf.createEntityManager()
    em.getTransaction().begin()
    em.persist(e)
    em.getTransaction().commit()
    Some(e)
  }

  def delete(uri: URI): Option[URI] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Node], uri) match {
        case n: Node =>
          em.getTransaction().begin()
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
}