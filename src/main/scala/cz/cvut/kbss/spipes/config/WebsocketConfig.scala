package cz.cvut.kbss.spipes.config

import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.11.16.
  */
@Configuration
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.websocket"))
class WebsocketConfig