import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.graph.Edge;
import cz.cvut.kbss.sempipes.model.graph.Graph;
import cz.cvut.kbss.sempipes.model.graph.Node;
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao;
import cz.cvut.kbss.sempipes.service.GraphService;
import org.junit.After;
import org.junit.Before;
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

    private static String GRAPH_URI_String = "https://graphs/911";
    private static URI GRAPH_URI = URI.create("https://graphs/911");


    @Autowired
    private GraphDao graphDao;

    @Autowired
    private GraphService graphService;

    @Before
    public void persistGraph() throws Exception {
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(URI.create("https://nodes/911"), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        final Edge e = new Edge(URI.create("https://edges/911"), n, n);
        final HashSet<Node> nodes = new HashSet<>();
        nodes.add(n);
        final HashSet<Edge> edges = new HashSet<>();
        edges.add(e);
        final Graph g = new Graph(GRAPH_URI, "Graph", nodes, edges);
        assertEquals(new Some<>(g), graphDao.add(g));
        assertEquals(graphDao.get(GRAPH_URI), graphService.getGraphByUri(GRAPH_URI_String));
    }

    @After
    public void deleteGraph() throws Exception {
        assertEquals(new Some<>(GRAPH_URI), graphDao.delete(GRAPH_URI));
        assertEquals(scala.None$.MODULE$, graphDao.get(GRAPH_URI));
    }

    @Test
    public void getAllGraphs() throws Exception {
        assertEquals(graphDao.getAll(), graphService.getAllGraphs());
    }

    @Test
    public void getGraph() throws Exception {
        assertEquals(graphDao.get(GRAPH_URI), graphService.getGraphByUri(GRAPH_URI_String));
    }

    @Test
    public void getNodes() throws Exception {
        assertEquals(graphDao.get(GRAPH_URI).get().getNodes().size(), graphService.getGraphNodes(GRAPH_URI_String).get().toSet().size());
    }

    @Test
    public void getEdges() throws Exception {
        Graph g = graphDao.get(GRAPH_URI).get();
        assertEquals(g.getEdges().size(), graphService.getGraphEdges(GRAPH_URI_String).get().toSet().size());
    }
}