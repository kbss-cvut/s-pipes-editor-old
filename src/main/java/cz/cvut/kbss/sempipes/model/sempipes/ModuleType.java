package cz.cvut.kbss.sempipes.model.sempipes;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
 */
@OWLClass(iri = Vocabulary.s_c_Module)
public class ModuleType {
    @Id(generated = true)
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_comment)
    private String comment;

    public ModuleType() {
    }

    public ModuleType(URI uri, String label) {
        this.uri = uri;
        this.label = label;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ModuleType{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
