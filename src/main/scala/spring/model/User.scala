package spring.model

import scala.beans.BeanProperty

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.2016.
  */
class User(
            @BeanProperty
            var id: Long,
            @BeanProperty
            var name: String) {
  def this() = this(-1, "")

  User.nextId += 1

  def this(u: User) =
    this(User nextId, u name)
}

object User {
  var nextId = 0

  def apply(name: String): User =
    new User(nextId, name)

  def apply(u: User) =
    new User(u)
}