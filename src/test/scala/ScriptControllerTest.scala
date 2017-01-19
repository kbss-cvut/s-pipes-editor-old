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
@ContextConfiguration(classes = Array(classOf[TestServiceConfig]))
@WebAppConfiguration
class ScriptControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var webApplicationContext: WebApplicationContext = _

  @Mock
  private var sempipesServiceMock: SempipesService = _

  private var mockMvc: MockMvc = _

  @Before
  def setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build
    MockitoAnnotations.initMocks(this)
  }


  @Test
  def getScripts() {
    when(sempipesServiceMock.getScripts("https://kbss.felk.cvut.cz/sempipes-sped/scripts")).thenReturn(None)
    mockMvc.perform(get("/rest/scripts")).andExpect(status.isNotFound)
  }

  @Test
  def getScriptReturnsNotFound() {
    val id = "1234"
    when(sempipesServiceMock.getScript("https://kbss.felk.cvut.cz/sempipes-sped/scripts/", id)).thenReturn(None)
    mockMvc.perform(get("/rest/scripts/" + id)).andExpect(status().isNotFound)
  }
}