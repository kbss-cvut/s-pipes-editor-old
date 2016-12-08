package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.sempipes.model.graph.Edge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 08.12.16.
  */
@Repository
class EdgeDao {

  @Autowired
  private var emf: EntityManagerFactory = _

  def getEdge(uri: URI): Edge = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Edge], uri)
    }
    finally {
      em.close()
    }
  }

  def addEdge(e: Edge): Edge = {
    assert(e != null)
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    em.persist(e)
    em.getTransaction.commit()
    em.close()
    e
  }

  def addEdges(n: Traversable[Edge]): Traversable[Edge] = {
    assert(n != null && n.nonEmpty)
    n foreach addEdge
    n
  }

  def deleteEdge(uri: URI): URI = {
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    try {
      em.remove(em.find(classOf[Edge], uri))
      em.getTransaction.commit()
      uri
    }
    catch {
      case _: NullPointerException => null
    }
    finally {
      em.close()
      null
    }
  }

  def deleteEdges(n: Traversable[Edge]): Traversable[Edge] = {
    assert(n != null && n.nonEmpty)
    n foreach (e => deleteEdge(e.getUri))
    n
  }
}