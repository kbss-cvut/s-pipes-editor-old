package cz.cvut.kbss.spipes.model.spipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_next_dto;
import static cz.cvut.kbss.spipes.model.Vocabulary.s_p_has_source_uri;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.03.2018.
 */
@OWLClass(iri = s_c_next_dto)
public class NextDTO {

    @OWLDataProperty(iri = s_p_has_source_uri)
    private String sourceUri;

    @OWLDataProperty(iri = s_p_has_source_uri)
    private String targetUri;

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }
}
