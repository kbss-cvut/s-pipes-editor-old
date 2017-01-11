import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.graph.Edge;
import cz.cvut.kbss.sempipes.model.graph.Graph;
import cz.cvut.kbss.sempipes.model.graph.Node;
import cz.cvut.kbss.sempipes.model.sempipes.ModuleType;
import cz.cvut.kbss.sempipes.persistence.dao.DataStreamDao;
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.net.URI;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 17.11.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class RestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GraphDao graphDao;

    @Autowired
    private DataStreamDao dataStreamDao;

    private MockMvc mockMvc;

    private JsonLdDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        deserializer = JsonLdDeserializer.createExpandedDeserializer();
    }

    @Test
    public void getAllGraphs() throws Exception {
        ResultActions result = mockMvc.perform(get("/graphs/"));
        if (graphDao.getAll().isEmpty()) {
            result.andExpect(status().isNotFound());
            assertEquals("[]", result.andReturn().getResponse().getContentAsString());
        } else {
            result.andExpect(status().isOk());
        }
    }

    @Test
    public void getGraph() throws Exception {
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://nodes/1"), "Label", 1, 2, types, new java.util.HashSet<>(), new java.util.HashSet<String>());
        final Edge e = new Edge(new URI("https://edges/1"), n, n);
        final HashSet<Node> nodes = new HashSet<>();
        nodes.add(n);
        final HashSet<Edge> edges = new HashSet<>();
        edges.add(e);
        final Graph g1 = new Graph(new URI("https://graphs/1"), "Graph", nodes, edges);
        graphDao.delete(g1.getUri());
        graphDao.add(g1);
        ResultActions result = mockMvc.perform(get("/graphs/1"));
        result.andExpect(status().isOk());
        assertEquals("{\"uri\":\"https://graphs/1\",\"label\":\"Graph\",\"nodes\":[{\"uri\":\"https://nodes/1\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/2\",\"https://type/3\",\"https://type/1\"],\"inParameters\":[],\"outParameters\":[]}],\"edges\":[{\"uri\":\"https://edges/1\",\"sourceNode\":{\"uri\":\"https://nodes/1\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/2\",\"https://type/3\",\"https://type/1\"],\"inParameters\":[],\"outParameters\":[]},\"destinationNode\":{\"uri\":\"https://nodes/1\",\"label\":\"Label\",\"x\":1.0,\"y\":2.0,\"nodeTypes\":[\"https://type/2\",\"https://type/3\",\"https://type/1\"],\"inParameters\":[],\"outParameters\":[]}}]}",result.andReturn().getResponse().getContentAsString());
        graphDao.delete(g1.getUri());
    }

    @Test
    public void getModules() throws Exception {
        ResultActions result = mockMvc.perform(get("/sempipes/contexts/12/modules"));
        result.andExpect(status().isOk());
        String url = "https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data";
        Option<Traversable<ModuleType>> data = dataStreamDao.getModuleTypes(url);
        assertNotEquals(scala.None$.MODULE$, data);
        assertEquals(result.andReturn().getResponse().getContentAsString().split("@type").length, data.get().size());
    }
}
