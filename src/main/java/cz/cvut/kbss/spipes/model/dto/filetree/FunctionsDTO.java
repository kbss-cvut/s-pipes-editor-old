package cz.cvut.kbss.spipes.model.dto.filetree;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.spipes.model.AbstractEntity;
import cz.cvut.kbss.spipes.model.dto.FunctionDTO;

import java.util.List;

import static cz.cvut.kbss.spipes.model.Vocabulary.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.06.2018.
 */
@OWLClass(iri = s_c_functions_dto)
public class FunctionsDTO extends AbstractEntity {

    @OWLDataProperty(iri = s_p_has_script_path)
    private String scriptPath;

    @Sequence
    @OWLObjectProperty(iri = s_p_has_function_dto, fetch = FetchType.EAGER)
    private List<FunctionDTO> functions;

    public FunctionsDTO() {
    }

    public FunctionsDTO(String scriptPath, List<FunctionDTO> functions) {
        this.scriptPath = scriptPath;
        this.functions = functions;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public List<FunctionDTO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionDTO> functions) {
        this.functions = functions;
    }
}
