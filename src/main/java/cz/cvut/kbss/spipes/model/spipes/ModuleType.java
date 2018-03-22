package cz.cvut.kbss.spipes.model.spipes;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import static cz.cvut.kbss.spipes.model.Vocabulary.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
 */
@OWLClass(iri = s_c_Module)
public class ModuleType extends AbstractEntity {

    @OWLDataProperty(iri = s_p_label)
    private String label;
    @OWLDataProperty(iri = s_p_comment)
    private String comment;
    @OWLDataProperty(iri = s_p_icon)
    private String icon;

    public ModuleType() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
