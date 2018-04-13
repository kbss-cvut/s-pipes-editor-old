package cz.cvut.kbss.spipes.util

import cz.cvut.kbss.jopa.model.EntityManager
import org.apache.jena.query.Dataset

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 02.08.2017.
  */
object JopaPersistenceUtils {
  def getDataset(entityManager: EntityManager): Dataset =
    try
      entityManager.unwrap(classOf[Dataset])
    finally
      entityManager.close()
}