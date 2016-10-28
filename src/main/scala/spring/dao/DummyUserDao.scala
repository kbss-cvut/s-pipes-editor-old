package spring.dao

import org.springframework.stereotype.Component
import spring.model.User

import scala.collection.mutable.ListBuffer

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.2016.
  */
@Component
class DummyUserDao {


  def list = DummyUserDao.users

  def get(id: Long): Option[User] =
    list find (_.id == id)

  def create(u: User): User = {
    val user = User(u)
    DummyUserDao.users += user
    user
  }

  def delete(id: Long): Option[Long] =
    get(id) match {
      case Some(u) =>
        DummyUserDao.users -= u
        Some(id)
      case None => None
    }

  def update(id: Long, u: User) = {
    get(id) match {
      case Some(user) =>
        DummyUserDao.users -= user
        u id = id
        DummyUserDao.users += u
        u
      case None => None
    }
  }
}

object DummyUserDao {
  var users: ListBuffer[User] = new ListBuffer[User]()

  DummyUserDao.users += User("User 1")
  DummyUserDao.users += User("User 2")
  DummyUserDao.users += User("User 3")

}
