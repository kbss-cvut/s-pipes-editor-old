package testjava.rest;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testjava.service.XService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
public class XControllerTest extends XBaseControllerTestRunner{

    @Autowired
    XService xService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(xService);
    }


    @Test
    public void test() throws Exception {

        when(xService.getHelloMessageByService()).thenReturn("yourself");
        final MvcResult result = mockMvc.perform(get("/xcontrolers" )).andReturn();
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse().getStatus()));

        String message = result.getResponse().getContentAsString();
        assertTrue(message.contains("yourself"));
        System.out.println("Test failed succesfully");
    }

}
