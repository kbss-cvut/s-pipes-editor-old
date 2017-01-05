import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.graph.Edge;
import cz.cvut.kbss.sempipes.model.graph.Graph;
import cz.cvut.kbss.sempipes.model.graph.Node;
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao;
import cz.cvut.kbss.sempipes.service.GraphService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scala.Some;

import java.net.URI;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class ServiceTest {

    private static URI URI;

    @Autowired
    private GraphDao graphDao;

    @Autowired
    private GraphService graphService;

    @BeforeClass
    public static void initializeUri() throws Exception {
        URI = new URI("https://graphs/911");
    }

    @Before
    public void persistGraph() throws Exception {
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://nodes/911"), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        final Edge e = new Edge(new URI("https://edges/911"), n, n);
        final HashSet<Node> nodes = new HashSet<>();
        nodes.add(n);
        final HashSet<Edge> edges = new HashSet<>();
        edges.add(e);
        final Graph g = new Graph(URI, "Graph", nodes, edges);
        assertEquals(new Some<>(g), graphDao.add(g));
        assertEquals(graphDao.get(URI), graphService.getGraphByUri(URI));
    }

    @After
    public void deleteGraph() throws Exception {
        assertEquals(new Some<>(URI), graphDao.delete(URI));
        assertEquals(scala.None$.MODULE$, graphDao.get(URI));
    }

    @Test
    public void getAllGraphs() throws Exception {
        assertEquals(graphDao.getAll(), graphService.getAllGraphs());
    }

    @Test
    public void getGraph() throws Exception {
        assertEquals(graphDao.get(URI), graphService.getGraphByUri(URI));
    }

    @Test
    public void getNodes() throws Exception {
        assertEquals(graphDao.get(URI).get().getNodes(), graphService.getGraphNodes(URI));
    }

    @Test
    public void getEdges() throws Exception {
        assertEquals(graphDao.get(URI).get().getEdges(), graphService.getGraphEdges(URI));
    }
}