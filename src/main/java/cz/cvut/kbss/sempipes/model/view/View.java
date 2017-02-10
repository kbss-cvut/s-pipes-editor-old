package cz.cvut.kbss.sempipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 01.12.16.
 */
@OWLClass(iri = Vocabulary.s_c_view)
public class View extends AbstractEntity {


    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLObjectProperty(iri = Vocabulary.s_p_consists_of_node, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Node> nodes;
    @OWLObjectProperty(iri = Vocabulary.s_p_consists_of_edge, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Edge> edges;
    @OWLDataProperty(iri = Vocabulary.s_p_has_context_hash)
    private String contentHash;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_author)
    private Person author;

    public View() {
    }

    public View(String label, Set<Node> nodes, Set<Edge> edges) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_view + id);
        this.label = label;
        this.edges = edges;
        this.nodes = nodes;
    }

    public View(URI uri, String id, String label, Set<Node> nodes, Set<Edge> edges, String contentHash, Person author) {
        this.uri = uri;
        this.id = id;
        this.label = label;
        this.nodes = nodes;
        this.edges = edges;
        this.contentHash = contentHash;
        this.author = author;
    }

    public URI getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        View view = (View) o;

        if (getNodes() != null ? !getNodes().equals(view.getNodes()) : view.getNodes() != null) return false;
        return getEdges() != null ? getEdges().equals(view.getEdges()) : view.getEdges() == null;
    }

    @Override
    public int hashCode() {
        int result = getNodes() != null ? getNodes().hashCode() : 0;
        result = 31 * result + (getEdges() != null ? getEdges().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "View{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", nodes=" + (nodes == null ? "null" : nodes.toString()) +
                ", edges=" + (edges == null ? "null" : edges.toString()) +
                '}';
    }
}
