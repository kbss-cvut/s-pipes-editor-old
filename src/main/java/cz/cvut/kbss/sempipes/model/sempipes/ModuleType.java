package cz.cvut.kbss.sempipes.model.sempipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
 */
@OWLClass(iri = Vocabulary.s_c_Module)
public class ModuleType extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_comment)
    private String comment;

    public ModuleType() {
    }

    public ModuleType(String label, String comment) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_Module + "/" + id);
        this.label = label;
        this.comment = comment;
    }

    public ModuleType(URI uri, String id, String label, String comment) {
        this.uri = uri;
        this.id = id;
        this.label = label;
        this.comment = comment;
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
                ", id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
