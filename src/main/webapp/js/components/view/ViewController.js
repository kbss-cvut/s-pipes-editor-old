/**
 * Created by yan on 19.06.17.
 */

'use strict';

import React from "react";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Messager from "../wrapper/Messager";
var RoutingRules = require('../../utils/RoutingRules');
var Routes = require('../../utils/Routes');

class ViewController extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return <div id="view"></div>;
    }

    componentDidMount() {
        renderView();
        return null;
    }
}


// load data and render elements
function renderView() {

    function viewport() {
        var e = window,
            a = 'inner';
        if (!('innerWidth' in window)) {
            a = 'client';
            e = document.documentElement || document.body;
        }
        return {
            width: e[a + 'Width'],
            height: 900
        }
    }

    var width = viewport().width,
        height = viewport().height;

    var zoom = d3.behavior.zoom()
        .on("zoom", redraw);
    var svg = d3.select("#view")
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .call(zoom)
        .append("g");

// specify different layout options for the three modes
    var options = {
        fix: {
            algorithm: "de.cau.cs.kieler.fixed",
            layoutHierarchy: false
        },
        auto: {
            algorithm: "de.cau.cs.kieler.klay.layered",
            spacing: 10,
            layoutHierarchy: true,
            intCoordinates: true,
            direction: "RIGHT",
            edgeRouting: "ORTHOGONAL",
        },
        layer: {
            algorithm: "de.cau.cs.kieler.klay.layered",
            spacing: 10,
            layoutHierarchy: true,
            intCoordinates: true,
            direction: "RIGHT",
            edgeRouting: "ORTHOGONAL",
            cycleBreaking: "INTERACTIVE",
            nodeLayering: "INTERACTIVE",
        },
        order: {
            algorithm: "de.cau.cs.kieler.klay.layered",
            spacing: 10,
            layoutHierarchy: true,
            intCoordinates: true,
            direction: "RIGHT",
            edgeRouting: "ORTHOGONAL",
            crossMin: "INTERACTIVE",
        },
        layerOrder: {
            algorithm: "de.cau.cs.kieler.klay.layered",
            spacing: 10,
            layoutHierarchy: true,
            intCoordinates: true,
            direction: "RIGHT",
            edgeRouting: "ORTHOGONAL",
            cycleBreaking: "INTERACTIVE",
            nodeLayering: "INTERACTIVE",
            crossMin: "INTERACTIVE",
        }
    };

// define an arrow head
    svg.append("svg:defs")
        .append("svg:marker")
        .attr("id", "end")
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 10)
        .attr("refY", 0)
        .attr("markerWidth", 3)        // marker settings
        .attr("markerHeight", 5)
        .attr("orient", "auto")
        .style("fill", "#999")
        .style("stroke-opacity", 0.6)  // arrowhead color
        .append("svg:path")
        .attr("d", "M0,-5L10,0L0,5");

// group
    var root = svg.append("g");
    var layouter = klay.d3kgraph()
        .size([width, height])
        .transformGroup(root)
        .options(options.auto);

    var layoutGraph;

    d3.json("rest/json/new", function (error, graph) {

        layoutGraph = graph;

        layouter.on("finish", function (d) {

            var nodes = layouter.nodes();
            var links = layouter.links(nodes);

            // #1 add the nodes' groups
            var nodeData = root.selectAll(".node")
                .data(nodes, function (d) {
                    return d.id;
                });

            var node = nodeData.enter()
                .append("g")
                .attr("class", function (d) {
                    if (d.children)
                        return "node compound";
                    else
                        return "node leaf";
                });

            let leaves = document.getElementsByClassName("node leaf");
            for (let i = 0; i < leaves.length; i++) {
                leaves[i].addEventListener('dblclick', function (e) {

                    e.stopPropagation();

                    // RoutingRules.execute(Routes.createRecord.path)
                    switch (i % 5) {
                        case 1:
                            layoutOrderPreserve();
                            break;
                        case 2:
                            layoutLayerPreserve();
                            break;
                        case 3:
                            layoutLayerAndOrderPreserve();
                            break;
                        default:
                            layout()
                    }

                });
            }

            // add representing boxes for nodes
            var box = node.append("rect")
                .attr("class", "atom")
                .attr("width", 0)
                .attr("height", 0);

            // add node labels
            node.append("text")
                .attr("x", 2.5)
                .attr("y", 10.5)
                .text(function (d) {
                    return d.id;
                })
                .attr("font-size", "10px");


            // #2 add paths with arrows for the edges
            var linkData = root.selectAll(".link")
                .data(links, function (d) {
                    return d.id;
                });
            var link = linkData.enter()
                .append("path")
                .attr("class", "link")
                .attr("d", "M0 0")
                .attr("marker-end", "url(#end)");

            // #3 update positions of all elements

            // node positions
            nodeData.transition()
                .attr("transform", function (d) {
                    return "translate(" + (d.x || 0) + " " + (d.y || 0) + ")";
                });
            // node sizes
            nodeData.select(".atom")
                .transition()
                .attr("width", function (d) {
                    return d.width;
                })
                .attr("height", function (d) {
                    return d.height;
                });

            // edge routes
            linkData.transition().attr("d", function (d) {
                var path = "";
                if (d.sourcePoint && d.targetPoint) {
                    path += "M" + d.sourcePoint.x + " " + d.sourcePoint.y + " ";
                    (d.bendPoints || []).forEach(function (bp, i) {
                        path += "L" + bp.x + " " + bp.y + " ";
                    });
                    path += "L" + d.targetPoint.x + " " + d.targetPoint.y + " ";
                }
                return path;
            });

        });

        // start an initial layout
        layouter.kgraph(graph);
    })

    function redraw() {
        svg.attr("transform", "translate(" + d3.event.translate + ")"
            + " scale(" + d3.event.scale + ")");
    }

    function layoutFix() {
        applyInitialCoordinates(layoutGraph);
        clearBendpoints(layoutGraph);

        layouter.options(options.fix)
            .kgraph(layoutGraph);
    }

    function layoutLayerPreserve() {
        applyInitialCoordinates(layoutGraph);
        clearBendpoints(layoutGraph);

        layouter.options(options.layer)
            .kgraph(layoutGraph);
    }

    function layoutOrderPreserve() {
        applyInitialCoordinates(layoutGraph);
        clearBendpoints(layoutGraph);

        layouter.options(options.order)
            .kgraph(layoutGraph);
    }

    function layoutLayerAndOrderPreserve() {
        applyInitialCoordinates(layoutGraph);
        clearBendpoints(layoutGraph);

        layouter.options(options.layerOrder)
            .kgraph(layoutGraph);
    }

    function layout() {
        applyInitialCoordinates(layoutGraph);
        clearBendpoints(layoutGraph);

        layouter.options(options.auto)
            .kgraph(layoutGraph);
    }

    function clearBendpoints(parent) {
        if (parent.edges) {
            parent.edges.forEach(function (e) {
                e.sourcePoint = {x: 0, y: 0};
                e.targetPoint = {x: 0, y: 0};
                e.bendPoints = [];
            });
        }
        if (parent.children) {
            parent.children.forEach(function (c) {
                clearBendpoints(c);
            });
        }
    }

    function applyInitialCoordinates(parent) {
        if (parent.children) {
            parent.children.forEach(function (c) {

                if (c.properties && c.properties["de.cau.cs.kieler.position"]) {
                    var position = c.properties["de.cau.cs.kieler.position"].split(",");
                    c.x = parseInt(position[0]);
                    c.y = parseInt(position[1]);
                } else {
                    c.x = 0;
                    c.y = 0;
                }

                applyInitialCoordinates(c);
            });
        }
    }
}

export default injectIntl(I18nWrapper(Messager(ViewController)));