package cz.cvut.kbss.spipes.model.dto;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_module_dto;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_module_uri;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 31.03.2018.
 */
@MappedSuperclass
@OWLClass(iri = s_c_module_dto)
public class ModuleDTO extends ScriptDTO {

    @OWLDataProperty(iri = s_p_has_module_uri)
    private String moduleUri;

    public String getModuleUri() {
        return moduleUri;
    }

    public void setModuleUri(String moduleUri) {
        this.moduleUri = moduleUri;
    }
}
