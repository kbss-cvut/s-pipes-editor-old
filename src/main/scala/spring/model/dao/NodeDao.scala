package spring.model.dao

import org.springframework.stereotype.Repository
import spring.model.graph.Node

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 30.10.16.
  */
@Repository
class NodeDao {
  def getNodeById(id: Long) = Node(id)
}
