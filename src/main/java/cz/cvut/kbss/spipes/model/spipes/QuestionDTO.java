package cz.cvut.kbss.spipes.model.spipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;
import cz.cvut.kbss.spipes.model.Vocabulary;
import cz.cvut.sforms.model.Question;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.02.2018.
 */
@OWLClass(iri = Vocabulary.s_c_question_dto)
public class QuestionDTO extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_has_module_uri)
    private String module;

    @OWLDataProperty(iri = Vocabulary.s_p_has_module_type_uri)
    private String moduleType;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_root_question)
    private Question rootQuestion;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Question getRootQuestion() {
        return rootQuestion;
    }

    public void setRootQuestion(Question rootQuestion) {
        this.rootQuestion = rootQuestion;
    }
}
