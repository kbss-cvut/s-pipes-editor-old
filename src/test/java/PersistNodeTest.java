import cz.cvut.kbss.sempipes.config.RestConfig;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 03.12.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestConfig.class)
@WebAppConfiguration
public class PersistNodeTest {

    @Autowired
    private GraphDao graphDao;

    @Test
    public void persistNodeTest() throws Exception {
        graphDao.persistNode(graphDao.getNodeByURI(new URI("https://uri")));
    }
}
