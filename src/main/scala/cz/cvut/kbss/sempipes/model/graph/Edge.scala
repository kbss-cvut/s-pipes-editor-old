package spring.model.graph

import java.net.URI

import cz.cvut.kbss.jopa.model.annotations.{Id, OWLClass, OWLObjectProperty}
import cz.cvut.kbss.sempipes.model.Vocabulary

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.16.
  */
//@OWLClass(iri = Vocabulary.s_c_edge)
class Edge( @BeanProperty
            @Id(generated = true)
            uri: URI,
            @BeanProperty
            //@OWLObjectProperty(iri = Vocabulary.s_p_has_source_node)
            from: Node,
            //@OWLObjectProperty(iri = Vocabulary.s_p_has_destination_node)
            @BeanProperty to: Node) {

}

object Edge {
  def apply(from: Node, to: Node) = new Edge(new URI(""), from, to)
}
