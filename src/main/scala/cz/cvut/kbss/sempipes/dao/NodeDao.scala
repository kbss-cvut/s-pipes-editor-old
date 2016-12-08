package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.sempipes.model.graph.Node
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 08.12.16.
  */
@Repository
class NodeDao {

  @Autowired
  private var emf: EntityManagerFactory = _

  def getNode(uri: URI): Node = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Node], uri)
    }
    finally {
      em.close()
    }
  }

  def addNode(n: Node): Node = {
    assert(n != null)
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    em.persist(n)
    em.getTransaction.commit()
    em.close()
    n
  }

  def addNodes(n: Traversable[Node]): Traversable[Node] = {
    assert(n != null && n.nonEmpty)
    n foreach addNode
    n
  }

  def deleteNode(uri: URI): URI = {
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    try {
      em.remove(em.find(classOf[Node], uri))
      em.getTransaction.commit()
      uri
    }
    catch {
      case _: NullPointerException => null
    }
    finally {
      em.close()
    }
  }

  def deleteNodes(n: Traversable[Node]): Traversable[Node] = {
    assert(n != null && n.nonEmpty)
    n foreach (n => deleteNode(n.getUri))
    n
  }

  def updateNode(uri: URI, n: Node): URI = {
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    try {
      val node = em.find(classOf[Node], uri)
      node.setLabel(n.getLabel)
      node.setX(n.getX)
      node.setY(n.getY)
      node.setNodeTypes(n.getNodeTypes)
      em.getTransaction.commit()
      uri
    }
    finally {
      em.close()
      null
    }
  }
}