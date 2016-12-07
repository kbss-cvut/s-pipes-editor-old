package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManagerFactory
import cz.cvut.kbss.sempipes.model.graph.{Edge, Graph, Node}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao {

  @Autowired
  private var emf: EntityManagerFactory = _

  def getNodeByURI(uri: URI): Node = {
    val em = emf.createEntityManager()
    try {
      em.find(classOf[Node], uri)
    }
    finally {
      em.close()
    }
  }

  def persistNode(n: Node) = {
    assert(n != null)
    val em = emf.createEntityManager()
    em.getTransaction.begin()
    em.persist(n)
    em.getTransaction.commit()
    em.close()
  }

  def loadGraph(uri: URI) =
    new Graph(uri, "Label", MutableSet[Node]().asJava, MutableSet[Edge]().asJava)
}
