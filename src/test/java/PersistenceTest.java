import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.dao.EdgeDao;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import cz.cvut.kbss.sempipes.dao.NodeDao;
import cz.cvut.kbss.sempipes.model.graph.Edge;
import cz.cvut.kbss.sempipes.model.graph.Graph;
import cz.cvut.kbss.sempipes.model.graph.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.12.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestConfig.class)
@WebAppConfiguration
public class PersistenceTest {

    @Autowired
    private GraphDao graphDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private EdgeDao edgeDao;

    @Test
    public void edgePersistenceTest() throws Exception {
        URI uri = new URI("https://uri");
        edgeDao.deleteEdge(uri);
        assertNull(edgeDao.getEdge(uri));
        Node n = persistNode();
        Edge e = new Edge(uri, n, n);
        assertEquals(e, edgeDao.addEdge(e));
        assertEquals(e, edgeDao.getEdge(new URI("https://uri")));
        assertEquals(uri, edgeDao.deleteEdge(uri));
        assertNull(edgeDao.getEdge(uri));
    }

    @Test
    public void nodePersistenceTest() throws Exception {
        URI uri = new URI("https://uri");
        nodeDao.deleteNode(uri);
        assertNull(nodeDao.getNode(uri));
        Node n = persistNode();
        assertNotNull(nodeDao.getNode(uri));
        assertEquals(n, nodeDao.getNode(uri));
        assertEquals(uri, nodeDao.deleteNode(uri));
        assertNull(nodeDao.getNode(uri));
    }

    @Test
    public void graphPersistenceTest() throws Exception {
        final URI uri = new URI("https://uri");
        graphDao.deleteGraph(uri);
        assertNull(graphDao.getGraph(uri));
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://uri"), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        final Edge e = new Edge(new URI("https://edge"), n, n);
        final LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(n);
        final LinkedList<Edge> edges = new LinkedList<>();
        edges.add(e);
        final Graph g = new Graph(uri, "Graph", nodes, edges);
        assertEquals(g, graphDao.addGraph(g));
        assertNotNull(graphDao.getGraph(uri));
        assertEquals(g, graphDao.getGraph(uri));
        assertEquals(uri, graphDao.deleteGraph(uri));
        assertNull(graphDao.getGraph(uri));
    }

    private Node persistNode() throws Exception {
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://uri"), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        nodeDao.addNode(n);
        return n;
    }
}
