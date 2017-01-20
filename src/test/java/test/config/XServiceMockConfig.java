package test.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.service.XService;

import static org.mockito.Mockito.mockingDetails;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@Configuration
public class XServiceMockConfig {

    @Bean
    public XService getService() {
        return new XService();
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

//    @Bean
//    public XController getServiceMock() {
//        return new XController();
//    }

//
//    @Bean
//    public SempipesDao getDao() {
//        return new SempipesDao();
//    }

//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }
}
