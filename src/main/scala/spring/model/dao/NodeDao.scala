package spring.model.dao

import java.net.URI

import org.springframework.stereotype.Repository
import spring.model.graph.Node

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 30.10.16.
  */
@Repository
class NodeDao {
  def getNodeByUri(uri: URI) = Node(uri, 0, 0, None, None, None)
}
