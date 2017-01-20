package cz.cvut.kbss.sempipes.model.sempipes;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.01.17.
 */
@OWLClass(iri = Vocabulary.s_c_context)
public class Context {
    @Id
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_comment)
    private String comment;
    @OWLDataProperty(iri = Vocabulary.s_p_has_content_hash)
    private String contentHash;

    //todo: Possible REST-friendly identifier
    //private String key;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
