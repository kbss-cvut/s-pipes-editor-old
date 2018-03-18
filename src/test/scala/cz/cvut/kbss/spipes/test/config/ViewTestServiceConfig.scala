package cz.cvut.kbss.spipes.test.config

import cz.cvut.kbss.spipes.service.ScriptService
import org.mockito.Mockito
import org.springframework.context.annotation.{Bean, Primary}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.03.2018.
  */
class ViewTestServiceConfig extends TestServiceConfig {
  @Bean
  @Primary
  def getScriptService: ScriptService = Mockito.mock(classOf[ScriptService])
}
