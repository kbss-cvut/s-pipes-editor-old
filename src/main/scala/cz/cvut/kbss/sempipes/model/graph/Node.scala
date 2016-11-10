package spring.model.graph

import java.net.URI

import cz.cvut.kbss.jopa.model.annotations.{Id, OWLClass, OWLDataProperty, Types}
import cz.cvut.kbss.sempipes.model.Vocabulary

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
//@OWLClass(iri = Vocabulary.s_c_node)
class Node(@BeanProperty
           @Id(generated = true)
           uri: URI,
           @BeanProperty
           //@OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
           x: Double,
           @BeanProperty
           //@OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
           y: Double,
           @BeanProperty
           @Types
           nodeType: Set[String],
           @BeanProperty
           //@OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
           inParameters: Set[String],
           @BeanProperty
           //@OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
           outParameters: Set[String]) {}

object Node {
  def apply(uri: URI, x: Double, y: Double, nodeType: Set[String], inParameters: Set[String], outParameters: Set[String]): Node =
    new Node(uri, x, y, nodeType, inParameters, outParameters)
}
