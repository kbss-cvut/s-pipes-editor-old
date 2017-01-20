package testjava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import testjava.service.XService;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@RestController
@RequestMapping(value = "/xcontrolers")
public class XController {
    @Autowired
    XService service;


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getHelloMessageByController() {
        return service.getHelloMessageByService();
    }
}
