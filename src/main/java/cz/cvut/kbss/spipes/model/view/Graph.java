package cz.cvut.kbss.spipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_graph;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_view;


/**
 * Created by yan on 2/10/17.
 */
@OWLClass(iri = s_c_graph)
public class Graph extends AbstractEntity {

    @OWLObjectProperty(iri = s_p_has_view)
    private Set<View> views;

    public Graph() {
    }

    public Graph(URI uri, String id, Set<View> views) {
        this.id = id;
        this.views = views;
    }

    public Graph(Set<View> views) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(s_c_graph + "/" + id);
        this.views = views;
    }

    public Set<View> getViews() {
        return views;
    }

    public void setViews(Set<View> views) {
        this.views = views;
    }
}
