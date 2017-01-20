package testjava.config

import org.springframework.context.annotation.ComponentScan
import testjava.rest.XController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("test.rest"))
class YRestConfig {
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
