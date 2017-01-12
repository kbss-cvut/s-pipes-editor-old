package cz.cvut.kbss.sempipes.model.sempipes;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 12.01.17.
 */
@OWLClass(iri = Vocabulary.s_c_Modules)
public class Module {

    @Id(generated = true)
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_c_Modules)
    private Module next;

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
                ", label='" + label + '\'' +
                ", next=" + next +
                '}';
    }
}
