package cz.cvut.kbss.spipes.exception

import org.springframework.http.HttpStatus

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.07.2017.
  */
class SpipesException(val status: HttpStatus, val body: String) extends RuntimeException {

  override def toString: String = "Sempipes failed miserably with a status of " + status + " saying " + body
}