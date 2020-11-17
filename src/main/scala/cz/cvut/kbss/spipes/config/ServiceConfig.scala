package cz.cvut.kbss.spipes.config

import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.service"))
class ServiceConfig {

  @Bean
  def restTemplate = new RestTemplate()
}
