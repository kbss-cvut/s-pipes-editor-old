package cz.cvut.kbss.spipes.model.spipes.question;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;
import cz.cvut.kbss.spipes.model.Vocabulary;

import java.net.URI;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/11/17.
 */
@OWLClass(iri = Vocabulary.s_c_answer)
public class Answer extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_has_data_value)
    private String textValue;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_answer_origin)
    private URI origin;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_object_value)
    private URI codeValue;

    public Answer() {
    }

    public Answer(String textValue, URI origin, URI codeValue) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_answer + "/" + id);
        this.textValue = textValue;
        this.origin = origin;
        this.codeValue = codeValue;
    }

    public Answer(URI uri, String id, String textValue, URI origin, URI codeValue) {
        this.uri = uri;
        this.id = id;
        this.textValue = textValue;
        this.origin = origin;
        this.codeValue = codeValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public URI getOrigin() {
        return origin;
    }

    public void setOrigin(URI origin) {
        this.origin = origin;
    }

    public URI getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(URI codeValue) {
        this.codeValue = codeValue;
    }
}
