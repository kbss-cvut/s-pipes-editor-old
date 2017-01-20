package test.rest;

import cz.cvut.kbss.sempipes.persistence.dao.SempipesDao;
import cz.cvut.kbss.sempipes.service.SempipesService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import test.service.XService;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@RestController
public class XController {
    @Autowired
    XService service;
}
