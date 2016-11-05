package cz.cvut.kbss.sempipes.config

import org.springframework.context.annotation.{ComponentScan, Configuration}
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 28.10.2016.
  */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes"))
class AppConfig {

}
