package cz.cvut.kbss.spipes.rest.dto

import com.fasterxml.jackson.annotation.{JsonRawValue, JsonValue}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/10/17.
  */
class RawJson(val value: String) {

  @JsonValue
  @JsonRawValue
  def getValue: String = value

  override def toString: String = value

  def canEqual(other: Any): Boolean = other.isInstanceOf[RawJson]

  override def equals(other: Any): Boolean = other match {
    case that: RawJson =>
      (that canEqual this) &&
        value == that.value
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(value)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object RawJson {
  def apply(value: String): RawJson = new RawJson(value)
}