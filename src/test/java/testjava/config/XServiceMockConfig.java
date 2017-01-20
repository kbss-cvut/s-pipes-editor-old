package testjava.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import testjava.service.XService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@Configuration
public class XServiceMockConfig {

    @Bean
    public XService getService() {
        return mock(XService.class);
    }

    @Bean
    MockBeanFactory mockBeanFactory() {
        return new MockBeanFactory();
    }

    private static class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            return !mockingDetails(bean).isMock();
        }
    }
}
