package cz.cvut.kbss.sempipes.model.graph;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_edge)
public class Edge {

    @Id(generated = true)
    private URI uri;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_source_node)
    private Node sourceNode;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_destination_node)
    private Node destinationNode;

    public Edge() {
    }

    public Edge(URI uri, Node sourceNode, Node destinationNode) {
        this.uri = uri;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(Node destinationNode) {
        this.destinationNode = destinationNode;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "uri=" + uri +
                ", sourceNode=" + sourceNode +
                ", destinationNode=" + destinationNode +
                '}';
    }
}
