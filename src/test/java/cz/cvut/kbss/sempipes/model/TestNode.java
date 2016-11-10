package cz.cvut.kbss.sempipes.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;

import java.net.URI;
import java.util.Set;

/**
 * Created by Miroslav Blasko on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_node)
public class TestNode {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;

    @OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
    private Double x;

    @OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
    private Double y;

    @Types
    private Set<String> nodeType;
    @OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
    private Set<String> inParameters;
    @OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
    private Set<String> outParameters;

    @Override
    public String toString() {
        return "TestNode{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", nodeType=" + nodeType +
                ", inParameters=" + inParameters +
                ", outParameters=" + outParameters +
                '}';
    }
}