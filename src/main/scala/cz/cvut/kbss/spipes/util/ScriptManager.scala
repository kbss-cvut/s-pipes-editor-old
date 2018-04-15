package cz.cvut.kbss.spipes.util

import java.io.File

import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._

import scala.io.Source

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.04.2018.
  */
trait ScriptManager extends PropertySource {
  protected def discoverLocations: Array[File] = getProperty(SCRIPTS_LOCATION).split(";").map(new File(_))

  protected def ignored: Set[File] =
    discoverLocations.flatMap(f => {
      val ignoreFileName = f.getAbsolutePath() + "/.spipesignore"
      if (new File(ignoreFileName).exists()) {
        Source.fromFile(ignoreFileName).getLines().toList.distinct
          .map(new File(_))
          .filter(_.exists())
      }
      else
        Set[File]()
    }).toSet
}
