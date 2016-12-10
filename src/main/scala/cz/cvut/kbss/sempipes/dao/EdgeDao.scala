package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.Edge
import org.springframework.stereotype.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 08.12.16.
  */
@Repository
class EdgeDao extends BaseDao[Edge] {

  override def get(uri: URI): Option[Edge] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Edge], uri) match {
        case e: Edge => Some(e)
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
      em.find(classOf[Edge], uri) match {
        case e: Edge =>
          em.remove(e)
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