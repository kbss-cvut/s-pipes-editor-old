package cz.cvut.kbss.sempipes.test.config

import cz.cvut.kbss.sempipes.persistence.dao.{GraphDao, SempipesDao}
import cz.cvut.kbss.sempipes.service.{GraphService, SempipesService}
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
class TestServiceConfig {

  @Bean
  def getSempipesService: SempipesService = Mockito.mock(classOf[SempipesService])

  @Bean
  def getGraphService: GraphService = Mockito.mock(classOf[GraphService])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }

}