package cz.cvut.kbss.sempipes.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.sempipes.TestVocabulary;

import java.net.URI;

/**
 * Created by Miroslav Blasko on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_node)
public class TestNode {

    @Id
    public URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;

}