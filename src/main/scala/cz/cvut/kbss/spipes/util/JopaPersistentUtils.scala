package cz.cvut.kbss.spipes.util

import cz.cvut.kbss.jopa.model.EntityManager
import org.openrdf.repository.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 02.08.2017.
  */
object JopaPersistenceUtils {
  def getRepository(entityManager: EntityManager): Repository =
    try
      entityManager.unwrap(classOf[Repository])
    finally
      entityManager.close()
}