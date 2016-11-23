package cz.cvut.kbss.sempipes.controller;

import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 23.11.16.
 */

@RestController
public class JavaTestController {

    @Autowired
    GraphDao graphDao;

    @GetMapping(path = "/javaTest", produces = JsonLd.MEDIA_TYPE)
    public ResponseEntity getNode() throws Exception {
        return new ResponseEntity<>(graphDao.getNodeByURI(new URI("/new/node")), HttpStatus.OK);
    }
}