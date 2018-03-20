package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.service.OntologyHelper
import org.mockito.Mockito
import org.springframework.context.annotation.{Bean, Primary}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
class ScriptTestServiceConfig extends TestServiceConfig {

  @Bean
  @Primary
  def getOntologyHelper: OntologyHelper = Mockito.mock(classOf[OntologyHelper])
}