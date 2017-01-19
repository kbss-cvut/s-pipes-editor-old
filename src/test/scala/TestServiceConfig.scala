import cz.cvut.kbss.sempipes.rest.ScriptController
import org.mockito.InjectMocks
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.01.17.
  */
@Configuration
class TestServiceConfig {

  @InjectMocks
  private var scriptController: ScriptController = _

  @Bean
  def getController: ScriptController = scriptController

  @Bean
  def restTemplate = new RestTemplate()
}
