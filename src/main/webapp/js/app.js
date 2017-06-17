/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var I18nStore = require('./stores/I18nStore');
var addLocaleData = require('react-intl').addLocaleData;

var intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    var lang = navigator.language;
    // if (lang && lang === 'cs' || lang === 'cs-CZ' || lang === 'sk' || lang === 'sk-SK') {
    //     intlData = require('./i18n/cs');
    // } else {
    //     intlData = require('./i18n/en');
    // }
    intlData = require('./i18n/en');
}

selectLocalization();
I18nStore.setMessages(intlData.messages);

//Test comment

// Have the imports here, so that the I18nStore is initialized before any of the components which might need it
var React = require('react');
var ReactDOM = require('react-dom');
var Router = require('react-router').Router;
var Route = require('react-router').Route;
var IndexRoute = require('react-router').IndexRoute;
var IntlProvider = require('react-intl').IntlProvider;

var history = require('./utils/Routing').history;
var Routes = require('./utils/Routes');
var Actions = require('./actions/Actions');

var Login = require('./components/login/Login');
var MainView = require('./components/MainView');
var DashboardController = require('./components/dashboard/DashboardController');
var RecordController = require('./components/record/RecordController').default;
var RecordsController = require('./components/record/RecordsController').default;
var UsersController = require('./components/user/UsersController').default;
var UserController = require('./components/user/UserController').default;
var RoutingRules = require('./utils/RoutingRules');

function onRouteEnter() {
    RoutingRules.execute(this.path);
}

// Wrapping router in a React component to allow Intl to initialize
var App = React.createClass({
    render: function () {
        return <IntlProvider {...intlData}>
            <Router history={history}>
                <Route path='/' component={MainView}>
                    <IndexRoute component={DashboardController}/>
                    <Route path={Routes.login.path} onEnter={onRouteEnter} component={Login}/>
                    <Route path={Routes.dashboard.path} onEnter={onRouteEnter} component={DashboardController}/>
                    <Route path={Routes.users.path} onEnter={onRouteEnter} component={UsersController}/>
                    <Route path={Routes.createUser.path} onEnter={onRouteEnter} component={UserController}/>
                    <Route path={Routes.editUser.path} onEnter={onRouteEnter} component={UserController}/>
                    <Route path={Routes.records.path} onEnter={onRouteEnter} component={RecordsController}/>
                    <Route path={Routes.createRecord.path} onEnter={onRouteEnter} component={RecordController}/>
                    <Route path={Routes.editRecord.path} onEnter={onRouteEnter} component={RecordController}/>
                </Route>
            </Router>
        </IntlProvider>;
    }
});

Actions.loadCurrentUser();

// Pass intl data to the top-level component
ReactDOM.render(<App/>, document.getElementById('content'));

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
var svg = d3.select("#content1")
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

// load data and render elements
d3.json("interactive.json", function (error, graph) {

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
            .attr("onclick", "alert(12345)")
            .attr("class", function (d) {
                if (d.children)
                    return "node compound";
                else
                    return "node leaf";
            });

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
});

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

$.ajax("rest/json/new", {
    type: "GET",
    statusCode: {
        200: function (response) {
            console.log(response)
        }
    }
});