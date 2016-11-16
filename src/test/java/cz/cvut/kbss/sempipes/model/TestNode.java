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

    @Types
    private Set<String> types;

    @OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
    private Double x;

    @OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
    private Double y;

    @OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
    private Set<String> inParameters;
    @OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
    private Set<String> outParameters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestNode testNode = (TestNode) o;

        if (!uri.equals(testNode.uri)) return false;
        if (!label.equals(testNode.label)) return false;
        if (!x.equals(testNode.x)) return false;
        if (!y.equals(testNode.y)) return false;
        if (!inParameters.equals(testNode.inParameters)) return false;
        return outParameters.equals(testNode.outParameters);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + inParameters.hashCode();
        result = 31 * result + outParameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TestNode{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", types=" + types +
                ", x=" + x +
                ", y=" + y +
                ", inParameters=" + inParameters +
                ", outParameters=" + outParameters +
                '}';
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

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
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