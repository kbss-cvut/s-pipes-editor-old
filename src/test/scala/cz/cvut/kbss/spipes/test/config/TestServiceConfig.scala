package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.persistence.dao._
import cz.cvut.kbss.spipes.service.ViewService
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.client.RestTemplate

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
  def mockBeanFactory = new MockBeanFactory

  @Bean
  def t = new RestTemplate()

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }
}