package cz.cvut.kbss.spipes.dto

import com.fasterxml.jackson.annotation.{JsonRawValue, JsonValue}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/10/17.
  */
case class RawJson(value: String) {

  @JsonValue
  @JsonRawValue
  def getValue: String = value
}