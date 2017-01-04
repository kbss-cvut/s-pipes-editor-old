import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.graph.Edge;
import cz.cvut.kbss.sempipes.model.graph.Graph;
import cz.cvut.kbss.sempipes.model.graph.Node;
import cz.cvut.kbss.sempipes.persistence.dao.GraphDao;
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
import static org.junit.Assert.assertNotNull;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.12.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class PersistenceTest {

    private static byte nodeCount = 123;

    @Autowired
    private GraphDao graphDao;

    @Test
    public void getAndAddGraph() throws Exception {
        final URI uri = new URI("https://graphs/11");
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://nodes/11" + nodeCount), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        final Edge e = new Edge(new URI("https://edges/11"), n, n);
        final HashSet<Node> nodes = new HashSet<>();
        nodes.add(n);
        final HashSet<Edge> edges = new HashSet<>();
        edges.add(e);
        final Graph g = new Graph(uri, "Graph", nodes, edges);
        assertEquals(new Some<>(g), graphDao.add(g));
        assertNotNull(graphDao.get(uri));
        assertEquals(new Some<>(g), graphDao.get(uri));
        assertEquals(g.getNodes(), graphDao.get(uri).get().getNodes());
        assertEquals(new Some<>(uri), graphDao.delete(uri));
        assertEquals(scala.None$.MODULE$, graphDao.get(uri));
    }

    @Test
    public void getAllGraphs() throws Exception {
        final URI uri1 = new URI("https://graphs/1");
        final URI uri2 = new URI("https://graphs/2");
        final HashSet<String> types = new HashSet<>();
        types.add("https://type/1");
        types.add("https://type/2");
        types.add("https://type/3");
        final Node n = new Node(new URI("https://nodes/1" + nodeCount), "Label", 1, 2, types, new java.util.HashSet<String>(), new java.util.HashSet<String>());
        final Edge e = new Edge(new URI("https://nodes/1"), n, n);
        final HashSet<Node> nodes = new HashSet<>();
        nodes.add(n);
        final HashSet<Edge> edges = new HashSet<>();
        edges.add(e);
        final Graph g1 = new Graph(uri1, "Graph", nodes, edges);
        final Graph g2 = new Graph(uri2, "Graph", nodes, edges);
        assertEquals(new Some<>(g1), graphDao.add(g1));
        assertNotNull(graphDao.get(uri1));
        assertEquals(new Some<>(g2), graphDao.add(g2));
        assertNotNull(graphDao.get(uri2));
        int size =  graphDao.getAll().get().size();
        assertEquals(new Some<>(uri1), graphDao.delete(uri1));
        assertEquals(scala.None$.MODULE$, graphDao.get(uri1));
        assertEquals(size - 1, graphDao.getAll().get().size());
        assertEquals(new Some<>(uri2), graphDao.delete(uri2));
        assertEquals(scala.None$.MODULE$, graphDao.get(uri2));
        assertEquals(size - 2, graphDao.getAll().get().size());
    }


    @Test
    public void deleteNonExistentGraph() throws Exception {
        assertEquals(scala.None$.MODULE$, graphDao.delete(new URI("https://graph/that/does/not/exist")));
    }
}
