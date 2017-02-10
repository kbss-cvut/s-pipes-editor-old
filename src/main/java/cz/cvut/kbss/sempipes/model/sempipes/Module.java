package cz.cvut.kbss.sempipes.model.sempipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 12.01.17.
 */
@OWLClass(iri = Vocabulary.s_c_Modules)
public class Module extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_c_Modules)
    private Module next;

    public Module() {
    }

    public Module(String label, Module next) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_Modules + "/" + id);
        this.label = label;
        this.next = next;
    }

    public Module(URI uri, String id, String label, Module next) {
        this.uri = uri;
        this.id = id;
        this.label = label;
        this.next = next;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Module getNext() {
        return next;
    }

    public void setNext(Module next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Module{" +
                "uri=" + uri +
                ", id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", next=" + next +
                '}';
    }
}
