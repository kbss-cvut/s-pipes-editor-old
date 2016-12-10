package cz.cvut.kbss.sempipes.model.graph;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 01.12.16.
 */
@OWLClass(iri = Vocabulary.s_c_graph)
public class Graph {
    @Id(generated = true)
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @Sequence
    @OWLObjectProperty(iri = Vocabulary.s_p_consists_of_nodes, cascade = CascadeType.ALL)
    private Collection<Node> nodes;
    @Sequence
    @OWLObjectProperty(iri = Vocabulary.s_p_consists_of_edges, cascade = CascadeType.ALL)
    private Collection<Edge> edges;

    public Graph() {
    }

    public Graph(URI uri, String label, Collection<Node> nodes, Collection<Edge> edges) {
        this.uri = uri;
        this.label = label;
        this.edges = edges;
        this.nodes = nodes;
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

    public Collection<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Collection<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Graph graph = (Graph) o;

        if (!nodes.equals(graph.nodes)) return false;
        return edges.equals(graph.edges);
    }

    @Override
    public int hashCode() {
        int result = nodes.hashCode();
        result = 31 * result + edges.hashCode();
        return result;
    }
}
