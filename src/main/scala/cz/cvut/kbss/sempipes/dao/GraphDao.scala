package cz.cvut.kbss.sempipes.dao

import java.net.URI

import cz.cvut.kbss.sempipes.model.graph.Node
import org.springframework.stereotype.Repository

import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao {
  def getNodeByURI(uri: URI) =
    Node(uri, "Label", 0, 0, MutableSet(), MutableSet(), MutableSet())
}
