import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.sempipes.model.graph.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Miroslav Blasko on 10.11.16.
 */
public class JsonLD2JavaConverterTest {


    private JsonLdDeserializer deserializer;

    @Before
    public void setUp() {
        this.deserializer = JsonLdDeserializer.createExpandedDeserializer();
    }

    @Test
    public void testDeserializeNodeWithoutType() throws Exception {
        final Object input = readAndExpand("createNodeSample.json");
        final Node result = deserializer.deserialize(input, Node.class);
        Node control = new Node();
        control.setUri(new URI("/nodes/12034"));
        control.setLabel("create name");
        control.setX(1.0);
        control.setY(2.2);
        Set<String> in = new HashSet<>();
        in.add("a");
        in.add("b");
        Set<String> out = new HashSet<>();
        out.add("c");
        out.add("d");
        /*control.setInParameters(in);
        control.setOutParameters(out);*/

        assertTrue(result.equals(control));
    }


    private Object readAndExpand(String fileName) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        final Object jsonObject = JsonUtils.fromInputStream(is);
        return JsonLdProcessor.expand(jsonObject);
    }

    @Test
    public void testDeserializeNode() throws Exception {
        final Object input = readAndExpand("createNodeSample.json");
        final Node result = deserializer.deserialize(input, Node.class);
        assertNotNull(result);
        assertNotNull(result.getUri());
        assertNotNull(result.getLabel());
        assertNotNull(result.getX());
        assertNotNull(result.getY());
        /*assertNotNull(result.getInParameters());
        assertNotNull(result.getOutParameters());*/
        //assertTrue(result.getNodeTypes() != null); // this is not implemented in JSON-LD deserializer
    }

}
