package cz.cvut.kbss.spipes

import org.slf4j.LoggerFactory

import scala.reflect.ClassTag

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 17.02.2018.
  */
trait Logger[T] {
  protected final def log(implicit tag: ClassTag[T]): org.slf4j.Logger = LoggerFactory.getLogger(tag.runtimeClass)
}
