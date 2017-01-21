package cz.cvut.kbss.sempipes.test.config

import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 21.01.17.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes.rest"))
class TestRestConfig