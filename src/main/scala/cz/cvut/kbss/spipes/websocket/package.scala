package cz.cvut.kbss.spipes

import java.util.UUID

import javax.websocket.Session

import scala.collection.parallel.mutable

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 26.04.2018.
  */
package object websocket {
  private[websocket] val executions = mutable.ParHashMap[UUID, Session]()
}
