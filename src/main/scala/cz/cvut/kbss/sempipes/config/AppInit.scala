package cz.cvut.kbss.sempipes.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.2016.
  */
class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
  protected def getRootConfigClasses: Array[Class[_]] = Array[Class[_]](classOf[RestConfig], classOf[PersistenceConfig])

  protected def getServletConfigClasses: Array[Class[_]] = null

  protected def getServletMappings: Array[String] = Array[String]("/")
}