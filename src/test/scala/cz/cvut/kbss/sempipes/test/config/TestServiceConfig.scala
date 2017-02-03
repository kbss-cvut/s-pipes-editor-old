package cz.cvut.kbss.sempipes.test.config

import cz.cvut.kbss.sempipes.persistence.dao.{GraphDao, NodeDao, SempipesDao}
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes.service"))
class TestServiceConfig {

  @Bean
  def getGraphDao: GraphDao = Mockito.mock(classOf[GraphDao])

  @Bean
  def getNodeDao: NodeDao = Mockito.mock(classOf[NodeDao])

  @Bean
  def getSempipesDao: SempipesDao = Mockito.mock(classOf[SempipesDao])

  @Bean
  def getRestTemplate: RestTemplate = Mockito.mock(classOf[RestTemplate])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }
}