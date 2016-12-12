package cz.cvut.kbss.sempipes.controller;

import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.sempipes.dao.GraphDao;
import cz.cvut.kbss.sempipes.model.graph.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by Miroslav Blasko on 18.11.16.
 */
@RestController
@RequestMapping("/test")
public class NodeController {

    @Autowired
    GraphDao graphDao;

    @GetMapping(path = "/node", produces = JsonLd.MEDIA_TYPE)
    public Node getNode() {

        Node n = new Node();
        n.setUri(URI.create("http://test-id"));
        n.setLabel("ahoj");
        n.setX(1);
        n.setY(5);
//        n.setInParameters(new HashSet<>());
//        n.setNodeTypes(new HashSet<>());
//        n.setOutParameters(new HashSet<>());
        return n;
    }
}
