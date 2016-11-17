package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.Node
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao {
  def getNodeByURI(uri: URI) =
    new Node(uri, "Label", 1, 2, MutableSet[String]("type").asJava, MutableSet[String]("in").asJava, MutableSet[String]("out").asJava)
}
