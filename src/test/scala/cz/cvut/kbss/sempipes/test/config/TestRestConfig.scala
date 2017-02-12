package cz.cvut.kbss.sempipes.test.config

import cz.cvut.kbss.sempipes.service.{ViewService, QAService, SempipesService}
import org.mockito.Mockito
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes.rest"))
@EnableWebMvc
class TestRestConfig {

  @Bean
  def getSempipesService: SempipesService = Mockito.mock(classOf[SempipesService])

  @Bean
  def getViewService: ViewService = Mockito.mock(classOf[ViewService])

  @Bean
  def getNodeService: QAService = Mockito.mock(classOf[QAService])

  @Bean
  def mockBeanFactory = new MockBeanFactory

  class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean =
      !Mockito.mockingDetails(bean).isMock
  }
}