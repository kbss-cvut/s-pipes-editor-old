import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.sempipes.ModuleType;
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao;
import cz.cvut.kbss.sempipes.persistence.dao.SempipesDao;
import cz.cvut.kbss.sempipes.rest.BaseControllerTestRunner;
import cz.cvut.kbss.sempipes.service.SempipesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import scala.Option;
import scala.collection.Traversable;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 17.11.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class ScriptControllerTest extends BaseControllerTestRunner {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GraphDao graphDao;

    @InjectMocks
    private SempipesService sempipesServiceMock;

    @Autowired
    private SempipesDao dataStreamDao;

    private MockMvc mockMvc;

    private JsonLdDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        deserializer = JsonLdDeserializer.createExpandedDeserializer();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getModuleTypes() throws Exception {
        ResultActions result = mockMvc.perform(get("/sempipes/contexts/12/moduleTypes"));
        result.andExpect(status().isOk());
        String url = "https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data";
        Option<Traversable<ModuleType>> data = dataStreamDao.getModuleTypes(url);
        assertNotEquals(scala.None$.MODULE$, data);
        //todo Update the stupid test
        // assertEquals(result.andReturn().getResponse().getContentAsString().split("@type").length, data.get().size());
    }

    @Test
    public void getScripts() throws Exception {
        ResultActions result = mockMvc.perform(get("/scripts"));
        when(sempipesServiceMock.getScript("", "")).thenReturn(null);
        result.andExpect(status().isOk());
    }
}