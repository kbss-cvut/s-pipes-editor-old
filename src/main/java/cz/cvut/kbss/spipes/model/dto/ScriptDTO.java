package cz.cvut.kbss.spipes.model.dto;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_script_dto;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_script_path;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 31.03.2018.
 */
@MappedSuperclass
@OWLClass(iri = s_c_script_dto)
public class ScriptDTO extends AbstractEntity {

    @OWLDataProperty(iri = s_p_has_script_path)
    private String scriptPath;

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }
}
