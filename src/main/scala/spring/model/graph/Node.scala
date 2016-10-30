package spring.model.graph

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
class Node(@BeanProperty id: Long) {

}

object Node {
  def apply(id: Long): Node = new Node(id)
}
