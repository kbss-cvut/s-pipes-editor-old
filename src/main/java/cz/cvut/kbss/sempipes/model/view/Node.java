package cz.cvut.kbss.sempipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_node)
public class Node extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
    private Double x = 0.0;
    @OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
    private Double y = 0.0;
    @Types
    private Set<String> nodeTypes;
    @OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
    private Set<String> inParameters;
    @OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
    private Set<String> outParameters;

    public Node() {
    }

    public Node(String label, double x, double y, Set<String> nodeTypes, Set<String> inParameters, Set<String> outParameters) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_node + "/" + id);
        this.label = label;
        this.x = x;
        this.y = y;
        this.nodeTypes = nodeTypes;
        this.inParameters = inParameters;
        this.outParameters = outParameters;
    }

    public Node(URI uri, String id, String label, Double x, Double y, Set<String> nodeTypes, Set<String> inParameters, Set<String> outParameters) {
        this.uri = uri;
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
        this.nodeTypes = nodeTypes;
        this.inParameters = inParameters;
        this.outParameters = outParameters;
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

    public Set<String> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeTypes(Set<String> nodeTypes) {
        this.nodeTypes = nodeTypes;
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

    @Override
    public String toString() {
        return "Node{" +
                "uri=" + uri +
                ", id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", nodeTypes=" + nodeTypes +
                ", inParameters=" + inParameters +
                ", outParameters=" + outParameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (uri != null ? !uri.equals(node.uri) : node.uri != null) return false;
        if (id != null ? !id.equals(node.id) : node.id != null) return false;
        if (label != null ? !label.equals(node.label) : node.label != null) return false;
        if (x != null ? !x.equals(node.x) : node.x != null) return false;
        if (y != null ? !y.equals(node.y) : node.y != null) return false;
        if (nodeTypes != null ? !nodeTypes.equals(node.nodeTypes) : node.nodeTypes != null) return false;
        if (inParameters != null ? !inParameters.equals(node.inParameters) : node.inParameters != null) return false;
        return outParameters != null ? outParameters.equals(node.outParameters) : node.outParameters == null;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (nodeTypes != null ? nodeTypes.hashCode() : 0);
        result = 31 * result + (inParameters != null ? inParameters.hashCode() : 0);
        result = 31 * result + (outParameters != null ? outParameters.hashCode() : 0);
        return result;
    }
}
