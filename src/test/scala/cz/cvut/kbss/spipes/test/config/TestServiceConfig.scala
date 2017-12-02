package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.persistence.dao._
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.service"))
class TestServiceConfig {

  @Bean
  def getViewDao: ViewDao = Mockito.mock(classOf[ViewDao])

  @Bean
  def getspipesDao: ScriptDao = Mockito.mock(classOf[ScriptDao])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }
}