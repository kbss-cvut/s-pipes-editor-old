package cz.cvut.kbss.spipes.util

import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.sforms.model.{Answer, Question}

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 31.01.2018.
  */
object Implicits {
  implicit def question2String(q: Question): String =
    s"""
       |Question{
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
       |Answer{
       | id: ${a.getUri()},
       | types: ${a.getTypes()},
       | origin: ${a.getOrigin()},
       | codeValue: ${a.getCodeValue()},
       | textValue: ${a.getTextValue()}
       |}
      """.stripMargin

  implicit def entities2String(es: java.util.Set[_ <: cz.cvut.sforms.model.AbstractEntity]): String =
    es.asScala.mkString("[", ",\n", "]")

  implicit def entities2String(es: Traversable[_ <: AbstractEntity]): String =
    es.mkString("[", ",\n", "]")

  implicit def node2String(n: Node): String =
    s"""
       |View{
       | id: ${n.getUri()},
       | label: ${n.getLabel()},
       | moduleTypes: ${n.getModuleTypes},
       | x: ${n.getX()},
       | y: ${n.getY()},
       | inParameters: ${n.getInParameters()},
       | outParameters: ${n.getOutParameters()}
       |}
  """.stripMargin

  implicit def edge2String(e: Edge): String =
    s"""
       |View{
       | id: ${e.getUri()},
       | sourceNode: ${e.getSourceNode()},
       | destinationNode: ${e.getDestinationNode()},
       |}
  """.stripMargin

  implicit def view2String(v: View): String =
    s"""
       |View{
       | id: ${v.getUri()},
       | label: ${v.getLabel()},
       | author: ${v.getAuthor()},
       | contentHash: ${v.getContentHash()},
       | nodes: ${v.getNodes()},
       | edges: ${v.getEdges()}
       |}
  """.stripMargin
}