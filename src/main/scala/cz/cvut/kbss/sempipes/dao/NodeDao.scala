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
class NodeDao extends BaseDao[Node] {


  override def get(uri: URI): Option[Node] = {
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

  override def delete(uri: URI): Option[URI] = {
    val em = emf.createEntityManager()
    em.getTransaction().begin()
    try {
      em.find(classOf[Node], uri) match {
        case n: Node =>
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

  def update(uri: URI, e: Node): Option[URI] = {
    assert(e != null)
    val em = emf.createEntityManager()
    try {
      em.getTransaction().begin()
      em.find(classOf[Node], uri) match {
        case n: Node =>
          n.setLabel(e.getLabel)
          n.setX(e.getX)
          n.setY(e.getY)
          n.setNodeTypes(e.getNodeTypes)
          Some(uri)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }
}