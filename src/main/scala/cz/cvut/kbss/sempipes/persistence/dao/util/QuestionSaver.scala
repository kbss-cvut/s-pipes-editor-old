package cz.cvut.kbss.sempipes.persistence.dao.util

import java.net.URI

import cz.cvut.kbss.jopa.model.EntityManager
import cz.cvut.kbss.jopa.model.descriptors.Descriptor
import cz.cvut.kbss.sempipes.model.sempipes.question.Question


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/12/17.
  */
object QuestionSaver {

  def persist(q: Question, em: EntityManager): Unit = {
    def persistRec(q: Question, em: EntityManager, visisted: Set[URI]): Unit = {
      if (q != null)
        if (!visisted.contains(q.getUri()))
          em.persist(q)
      q.getSubQuestions().forEach(persistRec(_, em, visisted + q.getUri()))
    }

    persistRec(q, em, Set[URI]())
  }

  def persist(q: Question, em: EntityManager, d: Descriptor): Unit = {
    if (d == null)
      persist(q, em)
    else {
      def persistRec(q: Question, em: EntityManager, visisted: Set[URI], d: Descriptor): Unit = {
        if (q != null)
          if (!visisted.contains(q.getUri()))
            em.persist(q, d)
        q.getSubQuestions().forEach(persistRec(_, em, visisted + q.getUri(), d))

      }

      persistRec(q, em, Set[URI](), d)
    }
  }
}