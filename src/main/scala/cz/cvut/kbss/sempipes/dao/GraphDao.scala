package cz.cvut.kbss.sempipes.dao

import java.net.URI

import spring.model.graph.Node
import org.springframework.stereotype.Repository

import scala.collection.mutable.{Set => MutableSet}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.11.16.
  */
@Repository
class GraphDao {
  def getNodeByURI(uri: URI) =
    new Node(uri, 0, 0, Set(), Set(), Set())
}
