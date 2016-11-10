package cz.cvut.kbss.sempipes.model.graph;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.Set;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.11.16.
 */
public class Node {

    @Id(generated = true)
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
    private double x;
    @OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
    private double y;
    @Types
    private Set<String> nodeType;
    @OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
    private Set<String> inParameters;
    @OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
    private Set<String> outParameters;

    public Node(URI uri, String label, double x, double y, Set<String> nodeType, Set<String> inParameters, Set<String> outParameters) {
        this.uri = uri;
        this.label = label;
        this.x = x;
        this.y = y;
        this.nodeType = nodeType;
        this.inParameters = inParameters;
        this.outParameters = outParameters;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Set<String> getNodeType() {
        return nodeType;
    }

    public void setNodeType(Set<String> nodeType) {
        this.nodeType = nodeType;
    }

    public Set<String> getInParameters() {
        return inParameters;
    }

    public void setInParameters(Set<String> inParameters) {
        this.inParameters = inParameters;
    }

    public Set<String> getOutParameters() {
        return outParameters;
    }

    public void setOutParameters(Set<String> outParameters) {
        this.outParameters = outParameters;
    }
}
