package spring.model.graph

import java.net.URI

import cz.cvut.kbss.jopa.model.annotations.Id

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
class Edge(@Id(generated = true)
           uri: URI,
           @BeanProperty
           from: Node,
           @BeanProperty to: Node) {

}

object Edge {
  def apply(from: Node, to: Node) = new Edge(new URI(""),from, to)
}
