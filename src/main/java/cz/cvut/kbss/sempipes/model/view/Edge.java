package cz.cvut.kbss.sempipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_edge)
public class Edge extends AbstractEntity {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_source_node, cascade = CascadeType.ALL)
    private Node sourceNode;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_destination_node, cascade = CascadeType.ALL)
    private Node destinationNode;

    public Edge() {
    }

    public Edge(Node sourceNode, Node destinationNode) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_edge + id);
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public Edge(URI uri, String id, Node sourceNode, Node destinationNode) {
        this.uri = uri;
        this.id = id;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "uri=" + uri +
                ", sourceNode=" + sourceNode +
                ", destinationNode=" + destinationNode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (getSourceNode() != null ? !getSourceNode().equals(edge.getSourceNode()) : edge.getSourceNode() != null)
            return false;
        return getDestinationNode() != null ? getDestinationNode().equals(edge.getDestinationNode()) : edge.getDestinationNode() == null;
    }

    @Override
    public int hashCode() {
        int result = getSourceNode() != null ? getSourceNode().hashCode() : 0;
        result = 31 * result + (getDestinationNode() != null ? getDestinationNode().hashCode() : 0);
        return result;
    }
}
