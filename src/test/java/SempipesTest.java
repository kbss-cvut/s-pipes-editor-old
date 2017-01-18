import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.sempipes.ModuleType;
import cz.cvut.kbss.sempipes.service.SempipesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import scala.Option;
import scala.collection.Traversable;

import static org.junit.Assert.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 15.12.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class SempipesTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SempipesService sempipesService;

    @Test
    public void fssTest() throws Exception {
        final HttpEntity<Object> entity = new HttpEntity<>(null, new HttpHeaders());
        final ResponseEntity<String> result = restTemplate.exchange("https://kbss.felk.cvut.cz/sempipes-sped/service?_pId=generate-fss-form", HttpMethod.GET, entity,
                String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        final String type = result.getHeaders().getContentType().getType();
        final String subtype = result.getHeaders().getContentType().getSubtype();
        assertEquals(JsonLd.MEDIA_TYPE, type + "/" + subtype);
        assertNotNull(result.getBody());
    }

    @Test
    public void getModuleTypes() throws Exception {
        Option<Traversable<ModuleType>> modules = sempipesService.getModuleTypes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12");
        assertNotEquals(scala.None$.MODULE$, sempipesService.getModuleTypes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12"));
        assert modules.get().nonEmpty();
    }
}
