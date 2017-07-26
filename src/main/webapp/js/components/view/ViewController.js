/**
 * Created by yan on 19.06.17.
 */

'use strict';

import Actions from "../../actions/Actions";
import React from "react";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Messager from "../wrapper/Messager";
import {Button, ButtonGroup, Modal, OverlayTrigger, Popover, Tooltip} from "react-bootstrap";
import Record from "../record/Record";
import * as RouterStore from "../../stores/RouterStore";
import * as EntityFactory from "../../utils/EntityFactory";
import Mask from "../Mask";
import {ClipLoader} from "halogen";
import * as I18Store from "../../stores/I18nStore";

var RoutingRules = require('../../utils/RoutingRules');
var Routes = require('../../utils/Routes');
var Routing = require('../../utils/Routing');
var ModuleTypeStore = require('../../stores/ModuleTypeStore');

var that;

class ViewController extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            moduleTypes: null,
            loading: true,
            viewLoaded: false,
            modalVisible: false,
            formVisible: false,
            record: EntityFactory.initNewPatientRecord(),
            socket: new WebSocket("ws://localhost:8080/websocket")
        };
        this.state.socket.onmessage = () => this.onMessageReceived();
    }

    render() {
        if (this.state.loading)
            return (
                <Mask/>
            );
        return (
            <div id="main">
                <ButtonGroup vertical id="left-panel">
                    {this.state.moduleTypes.map((m) => {
                        let popover = (
                            <Popover
                                bsClass="module-type popover"
                                id={m["@id"]}
                                key={m["@id"]}
                                title={m["@id"]}>
                                {m["http://www.w3.org/2000/01/rdf-schema#comment"]}
                            </Popover>);
                        return <OverlayTrigger
                            placement="right"
                            key={m["@id"]}
                            overlay={popover}>
                            <Button block
                                    key={m["@id"]}>
                                {m["@id"].toString().split("/").reverse()[0]}
                            </Button>
                        </OverlayTrigger>
                    })}
                </ButtonGroup>
                <div id="view">
                    <div id="view-loading" hidden={this.state.viewLoaded}>
                        <div className='spinner-container'>
                            <div style={{width: 32, height: 32, margin: 'auto'}}>
                                <ClipLoader color='#337ab7' size='32px'/>
                            </div>
                            <div className='spinner-message'>{I18Store.i18n('view.loading-view')}</div>
                        </div>
                    </div>
                </div>
                <ButtonGroup vertical id="right-panel">
                    <OverlayTrigger placement="left"
                                    overlay={<Tooltip block
                                                      id="duplicate">
                                        {I18Store.i18n('view.duplicate-new-tab')}
                                    </Tooltip>}>
                        <Button bsStyle="info" onClick={() => window.open(location.href, '_blank')}>
                            {I18Store.i18n('view.duplicate')}
                        </Button>
                    </OverlayTrigger>
                    <Button bsStyle="danger"
                            onClick={() => renderView("fix")}>{I18Store.i18n('view.layout.fix')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => renderView("auto")}>{I18Store.i18n('view.layout.auto')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => renderView("layer")}>{I18Store.i18n('view.layout.layer')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => renderView("order")}>{I18Store.i18n('view.layout.order')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => renderView("layerOrder")}>{I18Store.i18n('view.layout.layerOrder')}</Button>

                </ButtonGroup>
                <Modal show={this.state.modalVisible}>
                    <Modal.Header>
                        <Modal.Title>{I18Store.i18n('view.script-changed')}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Button onClick={() => this.closeModal()}>{I18Store.i18n('view.ignore')}</Button>
                        <Button onClick={() => location.reload()}>{I18Store.i18n('view.reload')}</Button>
                    </Modal.Body>
                </Modal>
                <Modal dialogClassName="form-modal" show={this.state.formVisible}>
                    <Modal.Body>
                        <Record ref={(c) => this.recordComponent = c} handlers={handlers} record={this.state.record}
                                loading={this.state.loading}/>
                    </Modal.Body>
                </Modal>
            </div>);
        let handlers = {
            onCancel: this._onCancel,
            onChange: this._onChange
        };
    }

    componentWillMount() {
        Actions.loadAllModuleTypes();
    }

    componentDidMount() {
        that = this;
        this.unsubscribe = ModuleTypeStore.listen(this._moduleTypesLoaded);
    }

    _moduleTypesLoaded = (data) => {
        if (data.action === Actions.loadAllModuleTypes) {
            this.setState({moduleTypes: data.data, loading: false});
            renderView("auto");
        }
    };

    _onCancel = () => {
        let handlers = RouterStore.getViewHandlers(Routes.editRecord.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            this.setState({formVisible: false})
        }
    };

    _onChange = (change) => {
        var update = assign({}, this.state.record, change);
        this.setState({record: update});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    openForm() {
        this.setState({formVisible: true})
    }

    closeModal() {
        this.setState({modalVisible: false});
    }

    onMessageReceived() {
        this.setState({modalVisible: true});
    }
}


// load data and render elements
function renderView(layout) {

    that.setState({viewLoaded: false});

    var width = document.getElementById('view').offsetWidth,
        height = document.getElementById('view').offsetHeight;

    var zoom = d3.behavior.zoom()
        .on("zoom", redraw);
    d3.select("svg").remove();
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

                    that.openForm();

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

        that.setState({viewLoaded: true});
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

        switch (layout) {
            case "fix":
                layouter.options(options.fix).kgraph(layoutGraph);
                break;
            case "layer":
                layouter.options(options.layer).kgraph(layoutGraph);
                break;
            case "order":
                layouter.options(options.order).kgraph(layoutGraph);
                break;
            case "layerOrder":
                layouter.options(options.layerOrder).kgraph(layoutGraph);
                break;
            default:
                layouter.options(options.auto).kgraph(layoutGraph);
        }

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