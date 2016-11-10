package cz.cvut.kbss.sempipes.model;

import cz.cvut.kbss.jopa.CommonVocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.sempipes.TestVocabulary;

import java.net.URI;
import java.util.Set;

@OWLClass(iri = TestVocabulary.STUDY)
public class Study {

    @Id
    private URI uri;

    @OWLAnnotationProperty(iri = CommonVocabulary.RDFS_LABEL)
    private String name;

    @OWLObjectProperty(iri = TestVocabulary.HAS_MEMBER)
    private Set<Employee> members;

    @OWLObjectProperty(iri = TestVocabulary.HAS_PARTICIPANT)
    private Set<Employee> participants;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getMembers() {
        return members;
    }

    public void setMembers(Set<Employee> members) {
        this.members = members;
    }

    public Set<Employee> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Employee> participants) {
        this.participants = participants;
    }
}
