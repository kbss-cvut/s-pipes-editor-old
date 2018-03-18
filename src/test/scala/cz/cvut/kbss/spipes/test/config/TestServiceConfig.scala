package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.persistence.dao._
import cz.cvut.kbss.spipes.service.{ScriptService, ViewService}
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, Primary}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[ViewService]))
class TestServiceConfig {

  @Bean
  def getViewDao: ViewDao = Mockito.mock(classOf[ViewDao])

  @Bean
  def getspipesDao: ScriptDao = Mockito.mock(classOf[ScriptDao])

  @Bean
  @Primary
  def getScriptService: ScriptService = Mockito.mock(classOf[ScriptService])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }
}