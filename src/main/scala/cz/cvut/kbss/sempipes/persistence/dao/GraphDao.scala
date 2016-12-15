package cz.cvut.kbss.sempipes.persistence.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.Vocabulary
import cz.cvut.kbss.sempipes.model.graph.Graph
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao extends BaseDao[Graph] {

  override def get(uri: URI): Option[Graph] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Graph], uri) match {
        case g: Graph => Some(g)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }

  def getAll(): Option[Traversable[Graph]] = {
    val em = emf.createEntityManager()
    try {
      em.createNativeQuery("select ?s ?p ?o where { ?s a <http://onto.fel.cvut.cz/ontologies/sempipes/graph> }", classOf[Graph])
        //.setParameter("type", Vocabulary.s_c_graph)
        .getResultList() match {
        case nonEmpty: java.util.List[Graph] if !nonEmpty.isEmpty =>
          Some(nonEmpty.asScala)
        case empty: java.util.List[Graph] if empty.isEmpty =>
          None
      }
    }
    finally {
      em.close()
    }
  }

  override def delete(uri: URI): Option[URI] = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Graph], uri) match {
        case n: Graph =>
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

  def update(uri: URI, other: Graph): Option[Graph] = {
    assert(other != null)
    val em = emf.createEntityManager()
    try {
      em.getTransaction().begin()
      em.find(classOf[Graph], uri) match {
        case g: Graph =>
          g.setLabel(other.getLabel)
          g.setNodes(other.getNodes)
          g.setEdges(other.getEdges)
          Some(g)
        case null => None
      }
    }
    finally {
      em.close()
    }
  }
}