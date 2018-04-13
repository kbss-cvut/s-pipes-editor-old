package cz.cvut.kbss.spipes.util

import java.io.File


/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 12.04.2018.
  */
object Exceptions {

  class ScriptsNotFoundException extends RuntimeException

  class OntologyNotFoundException(file: File) extends RuntimeException {
    override def getMessage: String = f"""Ontology not found in file $file"""
  }

}
