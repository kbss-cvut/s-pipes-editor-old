import cz.cvut.kbss.sempipes.config.PersistenceConfig;
import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.model.sempipes.Context;
import cz.cvut.kbss.sempipes.model.sempipes.ModuleType;
import cz.cvut.kbss.sempipes.service.SempipesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scala.None$;
import scala.Option;
import scala.collection.Traversable;

import static org.junit.Assert.*;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 15.12.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, PersistenceConfig.class})
@WebAppConfiguration
public class SempipesServiceTest {

    @Autowired
    private SempipesService sempipesService;

    @Test
    public void getModuleTypes() throws Exception {
        Option<Traversable<ModuleType>> modules = sempipesService.getModuleTypes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12");
        assertNotEquals(scala.None$.MODULE$, sempipesService.getModuleTypes("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12"));
        assert modules.get().nonEmpty();
    }

    @Test
    public void getScripts() throws Exception {
        Option<Traversable<Context>> scripts = sempipesService.getScripts("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12");
        assertNotEquals(None$.MODULE$, scripts);
        assert scripts.get().nonEmpty();
    }
}
