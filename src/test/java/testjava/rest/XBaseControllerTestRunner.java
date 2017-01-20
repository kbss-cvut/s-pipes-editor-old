package testjava.rest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import testjava.config.XRestConfig;
import testjava.config.XServiceMockConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {XRestConfig.class, XServiceMockConfig.class})
@WebAppConfiguration
public abstract class XBaseControllerTestRunner {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

}
