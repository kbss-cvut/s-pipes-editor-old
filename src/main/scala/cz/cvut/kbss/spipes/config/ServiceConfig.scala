package cz.cvut.kbss.spipes.config

import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.service"))
class ServiceConfig
