package testjava.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@Configuration
@ComponentScan(basePackages = "testjava.rest")
public class XRestConfig {


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
