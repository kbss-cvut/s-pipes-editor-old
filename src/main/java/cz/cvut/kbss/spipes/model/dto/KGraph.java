package cz.cvut.kbss.spipes.model.dto;

import java.util.Set;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.04.2017.
 */
public class KGraph {
    private String id;
    private int x, y;
    private Set<Child> children;
    private Set<Edge> edges;

    public KGraph(String id, Set<Child> children, Set<Edge> edges) {
        this.id = id;
        this.children = children;
        this.edges = edges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
