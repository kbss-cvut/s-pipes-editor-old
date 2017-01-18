package cz.cvut.kbss.sempipes.model.graph;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.01.17.
 */
@OWLClass(iri = Vocabulary.s_c_user)
public class User {
    @Id
    private URI uri;
    private String login;
    private String passwordHash;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getUri().equals(user.getUri());
    }

    @Override
    public int hashCode() {
        return getUri().hashCode();
    }
}