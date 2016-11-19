import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.sempipes.controller.SemformsController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 19.11.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SemformsController.class)
@WebAppConfiguration
public class SemformsTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void ping() throws Exception {
        MvcResult result = mockMvc.perform(get("/semforms").
                content("Ping".getBytes()).
                contentType(JsonLd.MEDIA_TYPE)).
                andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(response, "Ping");
    }
}
