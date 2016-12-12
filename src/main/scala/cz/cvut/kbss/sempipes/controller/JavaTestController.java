package cz.cvut.kbss.sempipes.controller;

import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import cz.cvut.kbss.sempipes.model.graph.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping(path = "/javaTest2", produces = JsonLd.MEDIA_TYPE)
    @ResponseStatus(HttpStatus.OK)
    public Node getNode2() throws Exception {

        Node node = new Node();
        node.setUri(new URI("http://node/1"));
        node.setX(10);
        node.setY(12);

        return node;
    }
}