package cz.cvut.kbss.spipes.model.dto;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import static cz.cvut.kbss.spipes.model.Vocabulary.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 30.04.2018.
 */
@OWLClass(iri = s_c_function_dto)
public class FunctionDTO extends AbstractEntity {

    @OWLDataProperty(iri = s_p_has_function_local_name)
    private String functionLocalName;

    @OWLDataProperty(iri = s_p_has_function_uri)
    private String functionUri;

    public FunctionDTO() {
    }

    public FunctionDTO(String functionUri, String functionLocalName) {
        this.functionLocalName = functionLocalName;
        this.functionUri = functionUri;
    }

    public String getFunctionLocalName() {
        return functionLocalName;
    }

    public void setFunctionLocalName(String functionLocalName) {
        this.functionLocalName = functionLocalName;
    }

    public String getFunctionUri() {
        return functionUri;
    }

    public void setFunctionUri(String functionUri) {
        this.functionUri = functionUri;
    }
}
