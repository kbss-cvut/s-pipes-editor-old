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
import * as I18Store from "../../stores/I18nStore";

let Routes = require('../../utils/Routes');
let Routing = require('../../utils/Routing');
let ModuleTypeStore = require('../../stores/ModuleTypeStore');
let ViewStore = require('../../stores/ViewStore');
let ELK = require('elkjs');

let direction = 'RIGHT';
let defaultLayout = 'layered';
var that;

class ViewController extends React.Component {

    _getScript() {
        return "sample-script.ttl";
    }

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            moduleTypes: null,
            viewData: null,
            view: null,
            loading: true,
            viewLoaded: false,
            viewLaidOut: false,
            modalVisible: false,
            formVisible: false,
            record: EntityFactory.initNewPatientRecord(),
            socket: new WebSocket("ws://localhost:8080/websocket")
        };
        this.state.socket.onmessage = () => this.onMessageReceived();
        this.state.socket.onopen = () => this.state.socket.send(this._getScript());
    }

    render() {
        if (this.state.loading)
            return (
                <Mask/>
            );
        let handlers = {
            onCancel: this._onCancel,
            onChange: this._onChange
        };
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
                                    key={m["@id"]}
                                    onClick={() => this.addModule(m["@id"].toString().split("/").reverse()[0])}>
                                {m["@id"].toString().split("/").reverse()[0]}
                            </Button>
                        </OverlayTrigger>
                    })}
                </ButtonGroup>
                <div id="view">
                    <div id="editor"/>
                    <div hidden={this.state.viewLoaded}>
                        <Mask text={I18Store.i18n('view.loading-view')}/>
                    </div>
                    <div className='spinner-container' hidden={!this.state.viewLoaded || this.state.viewLaidOut}>
                        <Mask text={I18Store.i18n('view.laying-out-view')}/>
                    </div>
                </div>
                <canvas id="thumb" width="200" height="180"/>
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
                            onClick={() => this._renderView('fixed')}>{I18Store.i18n('view.layout.fixed')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('box')}>{I18Store.i18n('view.layout.box')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('layered')}>{I18Store.i18n('view.layout.layered')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('stress')}>{I18Store.i18n('view.layout.stress')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('mrtree')}>{I18Store.i18n('view.layout.mrtree')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('radial')}>{I18Store.i18n('view.layout.radial')}</Button>
                    <Button bsStyle="primary"
                            onClick={() => this._renderView('force')}>{I18Store.i18n('view.layout.force')}</Button>

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
    }

    componentWillMount() {
        Actions.loadAllModuleTypes(this._getScript());
    }

    componentDidMount() {
        that = this;
        this.unsubscribeModuleTypes = ModuleTypeStore.listen(this._moduleTypesLoaded);
        this.unsubscribeView = ViewStore.listen(this._viewDataLoaded);
    }

    _moduleTypesLoaded = (data) => {
        if (data.action === Actions.loadAllModuleTypes) {
            this.setState({moduleTypes: data.data, loading: false});
            Actions.loadViewData(this._getScript());
        }
    };

    _viewDataLoaded = (data) => {
        if (data.action === Actions.loadViewData) {
            this.setState({viewData: data.data, viewLoaded: true});
            this._renderView(defaultLayout);
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
        this.unsubscribeModuleTypes();
        this.unsubscribeView();
    }

    addModule(id) {
        this.state.view.startTransaction('loadgraph');
        this.state.view.addNode(id, 'basic', undefined);
        this.state.view.endTransaction('loadGraph');
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

    // TODO Find some actually usable layouts
    _renderView(algorithm) {

        this.setState({viewLaidOut: false});

        this.state.viewData.properties = {'elk.algorithm': algorithm, 'elk.direction': direction};

        var fbpGraph = window.TheGraph.fbpGraph;

        // The graph editor
        var editor = document.getElementById('editor');

        editor.className = "the-graph-dark";

        // Component library
        var library = {
            basic: {
                name: 'basic',
                description: 'basic demo component',
                icon: 'gear',
                inports: [
                    {'name': 'in', 'type': 'all'},
                ],
                outports: [
                    {'name': 'out', 'type': 'all'}
                ]
            },
        };

        // Load empty graph
        var graph = new fbpGraph.Graph();
        this.setState({view: graph});

        function renderEditor() {

            var props = {
                readonly: false,
                height: document.getElementById('editor').offsetHeight,
                width: document.getElementById('editor').offsetWidth,
                graph: graph,
                library: library,
            };
            var editor = document.getElementById('editor');
            editor.width = props.width;
            editor.height = props.height;
            var element = React.createElement(TheGraph.App, props);

            // Render the numbnail
            var thumb = document.getElementById('thumb');
            var properties = TheGraph.thumb.styleFromTheme('light');
            properties.width = thumb.width;
            properties.height = thumb.height;
            properties.nodeSize = 60;
            properties.lineWidth = 1;
            var context = thumb.getContext("2d");
            TheGraph.thumb.render(context, graph, properties);

            ReactDOM.render(element, editor);

            if (that.state !== undefined && that.state.viewData !== undefined) {
                delete that.state.viewData["children"];
                that.state.viewData["children"] = graph.nodes.map((n) => {
                    return {
                        id: n.id,
                        width: 100,
                        height: 100
                    }
                });
                delete that.state.viewData["edges"];
                that.state.viewData["edges"] = graph.edges.filter((e) => e !== undefined).map((e) => {
                    if (e !== undefined)
                        return {
                            id: e.from.node + e.to.node,
                            source: e.from.node,
                            target: e.to.node
                        };
                    return undefined;
                });
            }
        }

        graph.on('endTransaction', renderEditor); // graph changed
        window.addEventListener("resize", renderEditor);

        let elk = new ELK();
        let options = {
            'org.eclipse.elk.layered.crossingMinimization.strategy': 'INTERACTIVE',
        };
        elk.layout(this.state.viewData, options).then((g) => {
                graph.startTransaction('loadgraph');
                g["children"].map((m) => {

                    let metadata = {
                        label: m["id"],
                        x: m["x"],
                        y: m["y"]
                    };
                    graph.addNode(m["id"], 'basic', metadata);
                });
                g["edges"].map((e) => {
                    graph.addEdge(e["source"], 'out', e["target"], 'in', undefined);
                });
                graph.endTransaction('loadgraph');
                graph.nodes.map((n) => {
                    document.querySelectorAll('[title="' + n.id + '"]')[0].addEventListener('dblclick', function (e) {
                        e.stopPropagation();
                        that.openForm();
                    });
                });

                that.setState({viewLaidOut: true});
            },
            (err) => {
                console.log(err);
            });
    }
}

export default injectIntl(I18nWrapper(Messager(ViewController)));