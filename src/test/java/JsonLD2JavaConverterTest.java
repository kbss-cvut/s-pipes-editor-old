import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.sempipes.model.TestNode;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

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
    public void testDeserializeNode() throws Exception {
        final Object input = readAndExpand("createNodeSample.json");
        final TestNode result = deserializer.deserialize(input, TestNode.class);
//        verifyUserAttributes(USERS.get(HALSEY_URI), result);
//        assertNotNull(result.getEmployer());
//        verifyOrganizationAttributes(result.getEmployer());
        System.out.println("Test node " + result);
    }


    private Object readAndExpand(String fileName) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        final Object jsonObject = JsonUtils.fromInputStream(is);
        return JsonLdProcessor.expand(jsonObject);
    }
}
