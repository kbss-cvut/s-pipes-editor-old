package cz.cvut.kbss.spipes

import scala.util.{Failure, Success, Try}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.02.2018.
  */
trait ResourceManager {
  def cleanly[A, B](resource: A)(cleanup: A => Unit)(doWork: A => B): Try[B] = {
    try {
      Success(doWork(resource))
    }
    catch {
      case e: Exception =>
        Failure(e)
    }
    finally {
      cleanup(resource)
    }
  }
}
