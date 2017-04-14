package cz.cvut.kbss.spipes.util;

import cz.cvut.kbss.jopa.model.EntityManager;
import org.openrdf.repository.Repository;

/**
 * Created by Miroslav Blasko on 4.1.17.
 */
public class JopaPersistenceUtils {

    public static Repository getRepository(EntityManager entityManager) {
        try {
            return entityManager.unwrap(Repository.class);
        } finally {
            entityManager.close();
        }
    }
}
