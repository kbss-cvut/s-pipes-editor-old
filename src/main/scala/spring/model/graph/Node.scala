package spring.model.graph

import java.net.URI

import cz.cvut.kbss.jopa.model.annotations.Id

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
class Node(@Id(generated = true)
            @BeanProperty
           uri: URI,
           @BeanProperty
           x: Double,
           @BeanProperty
           y: Double,
           @BeanProperty
           nodeType: Any, // I have no idea what I'm doing
           @BeanProperty
           incoming: Option[Seq[String]],
           @BeanProperty
           outgoing: Option[Seq[String]]) {}

object Node {
  def apply(uri: URI, x: Double, y: Double, nodeType: Any, incoming: Option[Seq[String]], outgoing: Option[Seq[String]]): Node =
    new Node(uri, x, y, nodeType, incoming, outgoing)
}
