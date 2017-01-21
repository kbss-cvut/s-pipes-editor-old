package cz.cvut.kbss.sempipes.test.config

import cz.cvut.kbss.sempipes.test.service.TestService
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
class TestServiceConfig {

  @Bean
  def getService: TestService = Mockito.mock(classOf[TestService])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }

}