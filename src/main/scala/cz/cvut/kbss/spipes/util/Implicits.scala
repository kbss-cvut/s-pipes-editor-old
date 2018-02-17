package cz.cvut.kbss.spipes.util

import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.util.ConfigParam.ConfigValue
import cz.cvut.sforms.model.{Answer, Question}

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 31.01.2018.
  */
object Implicits {

  implicit def question2String(q: Question): String =
    s"""
       |Question {
       | id: ${q.getUri()},
       | types: ${q.getTypes()},
       | origin: ${q.getOrigin()}
       | label: ${q.getLabel()},
       | description: ${q.getDescription()},
       | layoutClass: ${q.getLayoutClass()},
       | subQuestions: ${q.getSubQuestions()},
       | answers: ${q.getAnswers()}
       |}
""".stripMargin

  implicit def answer2String(a: Answer): String =
    s"""
       |Answer {
       | id: ${a.getUri()},
       | types: ${a.getTypes()},
       | origin: ${a.getOrigin()},
       | codeValue: ${a.getCodeValue()},
       | textValue: ${a.getTextValue()}
       |}
      """.stripMargin

  implicit def entities2String(es: java.util.Set[_ <: AbstractEntity]): String =
    es.asScala.map(abstractEntity2String).mkString("[", ",\n", "]")

  implicit def entities2String(es: Traversable[_ <: AbstractEntity]): String =
    es.map(abstractEntity2String).mkString("[", ",\n", "]")

  implicit def abstractEntity2String(e: AbstractEntity): String =
    s"""
       |${e.getClass().getSimpleName()} {
       | ${e.getClass().getMethods().filter(_.getName().contains("get")).map((m) => m.getName() + ": " + m.invoke(e)).mkString(",\n ")}
       |}
     """.stripMargin

  implicit def configParamValue(c: ConfigValue): String = c.value
}