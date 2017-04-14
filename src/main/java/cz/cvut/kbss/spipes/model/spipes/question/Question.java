package cz.cvut.kbss.spipes.model.spipes.question;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.spipes.model.AbstractEntity;
import cz.cvut.kbss.spipes.model.Vocabulary;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/11/17.
 */
@OWLClass(iri = Vocabulary.s_c_question)
public class Question extends AbstractEntity {

    @Types
    private Set<String> types;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_question_origin)
    private URI questionOrigin;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_related_question, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Question> subQuestions;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_answer)
    private Set<Answer> answers;

    public Question() {
    }

    public Question(Set<String> types, URI questionOrigin, Set<Question> subQuestions, Set<Answer> answers) {
        this.id = UUID.randomUUID().toString();
        this.uri = URI.create(Vocabulary.s_c_question + "/" + id);
        this.types = types;
        this.questionOrigin = questionOrigin;
        this.subQuestions = subQuestions;
        this.answers = answers;
    }

    public Question(URI uri, String id, Set<String> types, URI questionOrigin, Set<Question> subQuestions, Set<Answer> answers) {
        this.uri = uri;
        this.id = id;
        this.types = types;
        this.questionOrigin = questionOrigin;
        this.subQuestions = subQuestions;
        this.answers = answers;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public URI getQuestionOrigin() {
        return questionOrigin;
    }

    public void setQuestionOrigin(URI questionOrigin) {
        this.questionOrigin = questionOrigin;
    }

    public Set<Question> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(Set<Question> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}