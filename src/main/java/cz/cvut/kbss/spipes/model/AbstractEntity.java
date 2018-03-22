package cz.cvut.kbss.spipes.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.net.URI;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_identifier;

/**
 * Created by yan on 2/10/17.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id(generated = true)
    protected URI uri;
    @OWLDataProperty(iri = s_p_identifier)
    protected String id;

    public URI getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }
}