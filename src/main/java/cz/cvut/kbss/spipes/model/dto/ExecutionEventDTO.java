package cz.cvut.kbss.spipes.model.dto;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_execution_event_dto;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_execution_id;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.04.2018.
 */
/* Unused */
@OWLClass(iri = s_c_execution_event_dto)
public class ExecutionEventDTO extends AbstractEntity {

    @OWLDataProperty(iri = s_p_has_execution_id)
    private String executionId;

    public ExecutionEventDTO() {
    }

    public ExecutionEventDTO(String executionId) {
        this.executionId = executionId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
