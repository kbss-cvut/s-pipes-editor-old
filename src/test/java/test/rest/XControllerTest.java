package test.rest;

import test.config.XRestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.config.XServiceMockConfig;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {XRestConfig.class, XServiceMockConfig.class})
public class XControllerTest {

    @Autowired
    XController xController;

    @Test
    public void test() {
        System.out.println("Test failed succesfully");
    }

}
