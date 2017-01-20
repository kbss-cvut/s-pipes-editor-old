package cz.cvut.kbss.sempipes.config

import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 07.12.16.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.sempipes.persistence"))
class PersistenceConfig