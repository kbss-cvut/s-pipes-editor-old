import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer
import cz.cvut.kbss.sempipes.config.{PersistenceConfig, RestConfig}
import cz.cvut.kbss.sempipes.rest.BaseControllerTestRunner
import cz.cvut.kbss.sempipes.service.SempipesService
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.Mockito.when
import org.mockito._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 18.01.17.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[RestConfig], classOf[PersistenceConfig]))
@WebAppConfiguration
class ScriptControllerScalaTest extends BaseControllerTestRunner {

  @Autowired
  private var webApplicationContext: WebApplicationContext = _

  @InjectMocks
  private var sempipesServiceMock: SempipesService = _

  private var mockMvc: MockMvc = _
  private var deserializer: JsonLdDeserializer = _

  @Before
  def setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build
    deserializer = JsonLdDeserializer.createExpandedDeserializer
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def getScriptReturnsNotFound() {
    val id = "1234"
    when(sempipesServiceMock.getScript("https://kbss.felk.cvut.cz/sempipes-sped/scripts/", id)).thenReturn(None)
    val result = mockMvc.perform(get("/scripts/" + id))
    result.andExpect(status().isNotFound)
  }
}