package cz.cvut.kbss.sempipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.sempipes.model.AbstractEntity;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.util.Set;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.01.17.
 */
@OWLClass(iri = Vocabulary.s_c_Person)
public class Person extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_username)
    private String username;

    @OWLDataProperty(iri = Vocabulary.s_p_has_password)
    private String password;

    @Types
    private Set<String> types;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}