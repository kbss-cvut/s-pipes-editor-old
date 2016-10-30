package spring.model.graph

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
class Edge(@BeanProperty from: Node, @BeanProperty to: Node) {

}

object Edge {
  def apply(from: Node, to: Node) = new Edge(from, to)
}
