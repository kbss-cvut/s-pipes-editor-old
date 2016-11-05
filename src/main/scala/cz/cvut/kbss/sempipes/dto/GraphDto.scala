package cz.cvut.kbss.sempipes.dto

import cz.cvut.kbss.sempipes.model.graph.{Edge, Node}
import org.springframework.stereotype.Repository

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 05.11.16.
  */
@Repository
class GraphDto {
  def persistNodes(nodes: Seq[Node]) =
    nodes map (n => {
      persistNode(n)
      n
    })

  def persistNode(node: Node): Node =
    node

  def deleteNodes(nodes: Seq[Node]) =
    nodes map (n => {
      deleteNode(n)
      n
    })

  def deleteNode(node: Node) =
    node

  def persistEdges(edges: Seq[Edge]) =
    edges map (e => {
      persistEdge(e)
      e
    })

  def persistEdge(edge: Edge): Edge =
    edge

  def deleteEdges(edges: Seq[Edge]) =
    edges map (e => {
      deleteEdge(e)
      e
    })

  def deleteEdge(edge: Edge) =
    edge
}
