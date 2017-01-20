package testjava.config

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import testjava.service.XService
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockingDetails

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
  */
@Configuration
object YServiceMockConfig {

  private class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    @throws[BeansException]
    override def postProcessAfterInstantiation(bean: Any, beanName: String): Boolean = !mockingDetails(bean).isMock
  }

}

@Configuration
class YServiceMockConfig {
  @Bean def getService: YService = mock(classOf[YService])

  @Bean private[config]
  def mockBeanFactory = new YServiceMockConfig.MockBeanFactory
}
