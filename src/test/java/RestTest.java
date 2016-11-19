import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import cz.cvut.kbss.sempipes.model.graph.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 17.11.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestConfig.class)
@WebAppConfiguration
public class RestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GraphDao graphDao;

    private MockMvc mockMvc;

    private JsonLdDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        deserializer = JsonLdDeserializer.createExpandedDeserializer();
    }

    @Test
    public void getTest() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void serializeNodeTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();
        String jsonLd = result.getResponse().getContentAsString();
        Node expectedNode = graphDao.getNodeByURI(new URI("/newNode"));

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writeValueAsString(expectedNode);
        assertEquals(jsonLd, expectedString);

        Object jsonLdObject = JsonUtils.fromString(jsonLd);
        Object expectedObject = JsonUtils.fromString(expectedString);

        assertEquals(jsonLdObject, expectedObject);
    }

    @Test
    public void deserializeNodeTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();
        String jsonLd = result.getResponse().getContentAsString();

        Object jsonLdObject = JsonUtils.fromString(jsonLd);
        List<Object> expanded = JsonLdProcessor.expand(jsonLdObject);
        deserializer.deserialize(expanded, Node.class);
    }

    @Test
    public void createNodeTest() throws Exception {
        mockMvc.perform(post("/nodes").contentType(JsonLd.MEDIA_TYPE).content("[]")).andExpect(status().isCreated());
    }
}
