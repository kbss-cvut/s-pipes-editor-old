package cz.cvut.kbss.spipes.util

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 02.08.2017.
  */
object ConfigParam extends Enumeration {

  sealed case class ConfigValue(value: String) extends Val

  final val REPOSITORY_URL = ConfigValue("repositoryUrl")
  final val DRIVER = ConfigValue("driver")
  final val SPIPES_LOCATION = ConfigValue("spipesLocation")
  final val SCRIPTS_LOCATION = ConfigValue("scriptsLocation")
  final val DEFAULT_CONTEXT = ConfigValue("defaultContext")
  final val EXECUTION_ENDPOINT = ConfigValue("executionEndpoint")
}