package cz.cvut.kbss.spipes.model.spipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static cz.cvut.kbss.spipes.model.Vocabulary.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
 */
@OWLClass(iri = s_c_Module)
public class ModuleType extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = s_p_label)
    private String label;
    @OWLDataProperty(iri = s_p_comment)
    private Set<String> comment;
    @OWLDataProperty(iri = s_p_icon)
    private String icon;

    public ModuleType() {
        uri = URI.create(String.format("%s/%s", s_c_Module, UUID.randomUUID().toString()));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<String> getComment() {
        return comment;
    }

    public void setComment(Set<String> comment) {
        this.comment = comment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
