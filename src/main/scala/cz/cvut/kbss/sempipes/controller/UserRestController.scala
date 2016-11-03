package spring.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._
import spring.dao.DummyUserDao
import spring.model.User

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.2016.
  */
@RestController
class UserRestController {

    @BeanProperty
    @Autowired
  var userDao: DummyUserDao = _

  @GetMapping(Array("/users"))
  def getUsers: java.util.List[User] =
    userDao list

  @GetMapping(Array("/users/{id}"))
  def getUser(@PathVariable("id") id: Long) = {
    userDao get id match {
      case Some(u) => new ResponseEntity(u, HttpStatus.OK)
      case None => new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND)
    }
  }

  @PostMapping(Array("/users"))
  def createUser(@RequestBody u: User) = {
    userDao create u
    new ResponseEntity(u, HttpStatus.OK)
  }

  @DeleteMapping(Array("users/{id}"))
  def deleteUser(@PathVariable("id") id: Long) = {
    userDao delete id match {
      case Some(long) => new ResponseEntity("Deleted user with ID" + id, HttpStatus.OK)
      case None => new ResponseEntity("No user found with ID " + id, HttpStatus.NOT_FOUND)
    }
  }

  @PutMapping(Array("/users/{id}"))
  def updateUser(@PathVariable("id") id: Long, @RequestBody u: User) =
    userDao update(id, u) match {
      case Some(user) => new ResponseEntity(user, HttpStatus.OK)
      case None => new ResponseEntity("No user found with ID " + id, HttpStatus.NOT_FOUND)
    }
}
