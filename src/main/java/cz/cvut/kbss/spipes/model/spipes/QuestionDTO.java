package cz.cvut.kbss.spipes.model.spipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;
import cz.cvut.sforms.model.Question;

import static cz.cvut.kbss.spipes.model.Vocabulary.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.02.2018.
 */
@OWLClass(iri = s_c_question_dto)
public class QuestionDTO extends AbstractEntity {

    @OWLDataProperty(iri = s_p_has_module_uri)
    private String module;

    @OWLDataProperty(iri = s_p_has_module_type_uri)
    private String moduleType;

    @OWLObjectProperty(iri = s_p_has_root_question)
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
