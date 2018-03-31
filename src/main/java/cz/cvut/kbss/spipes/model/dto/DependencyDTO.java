package cz.cvut.kbss.spipes.model.dto;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_dependency_dto;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_target_module_uri;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.03.2018.
 */
@OWLClass(iri = s_c_dependency_dto)
public class DependencyDTO extends ModuleDTO {

    @OWLDataProperty(iri = s_p_has_target_module_uri)
    private String targetModuleUri;

    public String getTargetModuleUri() {
        return targetModuleUri;
    }

    public void setTargetModuleUri(String targetModuleUri) {
        this.targetModuleUri = targetModuleUri;
    }
}
