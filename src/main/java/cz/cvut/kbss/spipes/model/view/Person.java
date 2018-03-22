package cz.cvut.kbss.spipes.model.view;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.spipes.model.AbstractEntity;

import static cz.cvut.kbss.spipes.model.Vocabulary.s_c_Person;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.01.17.
 */
@OWLClass(iri = s_c_Person)
public class Person extends AbstractEntity {
}