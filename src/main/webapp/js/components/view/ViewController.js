/**
 * Created by yan on 19.06.17.
 */


'use strict';

import Actions from '../../actions/Actions';
import React from 'react';
import injectIntl from '../../utils/injectIntl';
import I18nWrapper from '../../i18n/I18nWrapper';
import Messager from '../wrapper/Messager';
import {Button, ButtonGroup, Modal, OverlayTrigger, Tooltip} from 'react-bootstrap';
import Record from '../record/Record';
import * as RouterStore from '../../stores/RouterStore';
import * as EntityFactory from '../../utils/EntityFactory';
import Mask from '../Mask';
import * as I18Store from '../../stores/I18nStore';
import * as Utils from '../../utils/Utils';
import Typeahead from 'react-bootstrap-typeahead';
import ModuleTypeList from '../typeahead/ModuleTypeList';
import HTML5Backend from 'react-dnd-html5-backend';
import {DragDropContext} from 'react-dnd';
import ReactDOM from 'react-dom';
import ELK from 'elkjs';
import Hammer from 'hammerjs';
import Routes from '../../utils/Routes';
import Routing from '../../utils/Routing';
import ModuleTypeStore from '../../stores/ModuleTypeStore';
import ScriptStore from '../../stores/ScriptStore';
import ModuleStore from '../../stores/ModuleStore';
import ViewStore from '../../stores/ViewStore';
import QAStore from '../../stores/QAStore';
import FunctionList from '../typeahead/FunctionList';
import ResourceNotFound from "../ResourceNotFound";

function fixTheGraphGlobalDependece() {
    window.React = React;
    window.Hammer = Hammer;
    window.ReactDOM = ReactDOM;
}

fixTheGraphGlobalDependece();

let TheGraph = require("the-graph");

let direction = 'RIGHT';
let defaultLayout = 'layered';
let that;
let record;
let moduleTypeAhead;
let functionTypeAhead;

const TYPE = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-type";
const LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
const X = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-x-coordinate";
const Y = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-y-coordinate";
const EDGE = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/consists-of-edge";
const SOURCE_NODE = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-source-node";
const DESTINATION_NODE = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-destination-node";
const NODE = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/consists-of-node";
const ICON = "http://topbraid.org/sparqlmotion#icon";
const COMMENT = "http://www.w3.org/2000/01/rdf-schema#comment";
const ANSWERS = "http://onto.fel.cvut.cz/ontologies/documentation/has_answer";
const OBJECT_VALUE = "http://onto.fel.cvut.cz/ontologies/documentation/has_object_value";
const COMPONENT = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/component";
const FUNCTION_LOCAL_NAME = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-local-name";
const FUNCTION_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-uri";

class ViewController extends React.Component {

    _getScript() {
        if (this.props.location === undefined || this.props.location.state === undefined || this.props.location.state.script === undefined)
            return this.props.params.key.replace(/_/g, "/");
        return this.props.location.state.script;
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            error: null,
            moduleTypes: null,
            functions: null,
            view: null,
            moduleId: null,
            functionUri: null,
            type: null,
            coordinates: null,
            loading: true,
            viewLoaded: false,
            viewLaidOut: false,
            modalVisible: false,
            formVisible: false,
            record: EntityFactory.initNewRecord(),
            fsNotificationSocket: new WebSocket("ws://" + window.location.host + window.location.pathname + "notifications"),
            contextMenus: {
                main: null,
                selection: null,
                nodeInport: null,
                nodeOutport: null,
                graphInport: null,
                graphOutport: null,
                edge: {
                    icon: "long-arrow-right",
                    s4: {
                        icon: "trash-o",
                        iconLabel: "delete",
                        action: function (graph, itemKey, item) {
                            graph.removeEdge(item.from.node, item.from.port, item.to.node, item.to.port);
                        }
                    }
                },
                node: {
                    n4: {
                        icon: "cogs",
                        iconLabel: "configure",
                        action: function (graph, itemKey, item) {
                            that.openModuleDetails(itemKey, item.metadata.TYPE[0]);
                        }
                    },
                    s4: {
                        icon: "trash-o",
                        iconLabel: "delete",
                        action: function (graph, itemKey) {
                            that.deleteModule(itemKey);
                        }
                    }
                },
                group: {
                    icon: "th",
                    s4: {
                        icon: "trash-o",
                        iconLabel: "ungroup",
                        action: function (graph, itemKey) {
                            graph.removeGroup(itemKey);
                        }
                    }
                }
            },
            library: {
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
            }
        };
        if (!(Object.keys(props.location.query).length === 0))
            this.state.view = JSON.parse(localStorage.getItem(props.location.query["q"]));
        this.state.fsNotificationSocket.onmessage = () => this.onNotificationReceived();
        this.state.fsNotificationSocket.onopen = () => this.state.fsNotificationSocket.send(this._getScript());
    };

    render() {
        if (this.state.loading)
            return (
                <Mask/>
            );
        if (this.state.error)
            return <ResourceNotFound resource={this.state.error}/>;
        let handlers = {
            onCancel: this._onCancel,
            onChange: this._onChange,
            onSave: this.state.functionUri == null ? this._mergeModuleForm : this._mergeFunctionForm
        };
        record = <Record
            ref={(c) => this.recordComponent = c}
            handlers={handlers}
            record={this.state.record}
            script={this._getScript()}
            moduleType={this.state.type}
            module={this.state.moduleId}
            functionUri={this.state.functionUri}
            loading={this.state.loading}/>;
        moduleTypeAhead = <Typeahead
            allowReset={true}
            options={this.state.moduleTypes}
            displayOption={LABEL}
            filterOption={LABEL}
            optionsButton={true}
            onOptionSelected={(o, c) => this.openModuleDetails(null, o["@id"], c)}
            placeholder={I18Store.i18n('view.module-type')}
            customListComponent={ModuleTypeList}
        />;
        functionTypeAhead = undefined;
        if (this.state.functions != null)
            functionTypeAhead = <Typeahead
                allowReset={true}
                options={this.state.functions}
                displayOption={FUNCTION_LOCAL_NAME}
                filterOption={FUNCTION_URI}
                optionsButton={true}
                placeholder={I18Store.i18n('view.call-function')}
                onOptionSelected={o => this.openFunctionDetails(o[FUNCTION_URI])}
                customListComponent={FunctionList}
            />;

        return (
            <div id="main">
                <ButtonGroup vertical id="module-typeahead">
                    {moduleTypeAhead}
                    {functionTypeAhead}
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
                                    overlay={<Tooltip id="duplicate">
                                        {I18Store.i18n('view.duplicate-new-tab')}
                                    </Tooltip>}>
                        <Button bsStyle="info" onClick={this.duplicateTab}>
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
                        <Button onClick={() => {
                            this.closeModal();
                            this.setState({loading: true, view: null});
                            Actions.loadAllModuleTypes(this._getScript());
                        }}>{I18Store.i18n('view.reload')}</Button>
                    </Modal.Body>
                </Modal>
                <Modal dialogClassName="form-modal" show={this.state.formVisible}>
                    <Modal.Body>
                        {record}
                    </Modal.Body>
                </Modal>
            </div>);
    };

    componentWillMount() {
        Actions.loadAllModuleTypes(this._getScript());
    };

    componentDidMount() {
        that = this;
        this.unsubscribeModuleTypes = ModuleTypeStore.listen(this._moduleTypesLoaded);
        this.unsubscribeFunctions = ScriptStore.listen(this._functionsLoaded);
        this.unsubscribeModules = ModuleStore.listen(this._moduleTypesLoaded);
        this.unsubscribeView = ViewStore.listen(this._viewLoaded);
        this.unsubscribeQA = QAStore.listen(this._onCancel)
    };

    _moduleTypesLoaded = (data) => {
        if (data.action === Actions.loadAllModuleTypes) {
            if (data.data.status) {
                this.setState({loading: false, error: data.data.status + " " + data.data.response.body});
                return;
            }
            this.setState({moduleTypes: data.data});
            this.state.moduleTypes.map((m) => {
                this.state.library[m["@id"]] = {
                    name: m["@id"].toString().split("/").reverse()[0],
                    description: m[COMMENT],
                    icon: m[ICON] === undefined ? "gear" : m[ICON],
                    inports: [
                        {'name': 'in', 'type': 'all'},
                    ],
                    outports: [
                        {'name': 'out', 'type': 'all'}
                    ]
                };
            });
            Actions.listFunctions(this._getScript());
        }
    };

    _functionsLoaded = (data) => {
        this.setState({loading: false});
        if (!data.data.status) {
            this.setState({functions: data.data});
        }
        else {
            this.setState({functions: null});
        }
        if (this.state.view === null)
            Actions.loadView(this._getScript());
        else
            this.loadViewFromData(this.state.view);
    };

    loadViewFromData(data) {
        let fbpGraph = TheGraph.fbpGraph;
        let view = new fbpGraph.Graph();
        data.nodes.map((n) => view.addNode(n.id, n.component, n.metadata));
        data.edges.map((e) => view.addEdge(e.from.node, e.from.port, e.to.node, e.to.port, e.metadata));

        this.setState({view: view});
        this.setState({viewLoaded: true});
        this._renderView("none");
    }

    _viewLoaded = (data) => {
        if (data.action === Actions.loadView) {
            if (data.data.status) {
                this.setState({loading: false, error: data.data.status + " " + data.data.response.body});
            }
            this.setState({view: new TheGraph.fbpGraph.Graph()});
            data.data[NODE].map(n => {
                if (n[TYPE] !== undefined)
                    this.state.view.addNode(n["@id"], n[COMPONENT], {
                        label: n[LABEL] === undefined ?
                            n["@id"].toString().split("/").reverse()[0] :
                            n[LABEL],
                        TYPE: n[TYPE],
                        x: n[X],
                        y: n[Y]
                    })
            });
            data.data[EDGE].map(e => {
                if (e[SOURCE_NODE][TYPE] !== undefined) {
                    let n = e[SOURCE_NODE];
                    this.state.view.addNode(n["@id"], n[COMPONENT], {
                        label: n[LABEL] === undefined ?
                            n["@id"].toString().split("/").reverse()[0] :
                            n[LABEL],
                        TYPE: n[TYPE],
                        x: n[X],
                        y: n[Y]
                    })
                }
                if (e[DESTINATION_NODE][TYPE] !== undefined) {
                    let n = e[DESTINATION_NODE];
                    this.state.view.addNode(n["@id"], n[COMPONENT], {
                        label: n[LABEL] === undefined ?
                            n["@id"].toString().split("/").reverse()[0] :
                            n[LABEL],
                        TYPE: n[TYPE],
                        x: n[X],
                        y: n[Y]
                    })
                }
            });
            data.data[EDGE].map(e => {
                let from = typeof e[SOURCE_NODE] === "object" ?
                    e[SOURCE_NODE]["@id"] :
                    e[SOURCE_NODE];
                let to = typeof e[DESTINATION_NODE] === "object" ?
                    e[DESTINATION_NODE]["@id"] :
                    e[DESTINATION_NODE];
                this.state.view.addEdge(from, 'out', to, 'in', {id: e["@id"]});
            });
            this.setState({viewLoaded: true});
            this._renderView(defaultLayout);
            this.state.view.on("addEdge", e => {
                Actions.createDependency(this._getScript(), e.from.node, e.to.node);
            });
            this.state.view.on("removeEdge", e => {
                Actions.deleteDependency(this._getScript(), e.from.node, e.to.node);
            });
        }
    };

    _mergeModuleForm = () => {
        const formData = this.recordComponent.refs.wrappedInstance.getWrappedComponent().getFormData();
        const uriQ = Utils.findByOrigin(formData, "http://www.w3.org/2000/01/rdf-schema#Resource");
        const uri = uriQ[ANSWERS][0][OBJECT_VALUE]["@id"];
        Actions.saveModuleForm(this._getScript(), uri, this.state.type, formData);
        this.setState({formVisible: false, type: null, moduleId: null, coordinates: null});
    };

    _mergeFunctionForm = () => {
        const formData = this.recordComponent.refs.wrappedInstance.getWrappedComponent().getFormData();
        Actions.saveFunctionForm(this._getScript(), this.state.functionUri, formData);
        this.setState({formVisible: false, functionUri: null});
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
        let update = assign({}, this.state.record, change);
        this.setState({record: update});
    };

    componentWillUnmount() {
        this.unsubscribeModuleTypes();
        this.unsubscribeFunctions();
        this.unsubscribeModules();
        this.unsubscribeView();
    };


    openModuleDetails(moduleId, moduleType, coordinates) {
        this.setState({
            moduleId: moduleId,
            type: moduleType,
            functionUri: null,
            coordinates: coordinates,
            formVisible: true
        });
    };

    openFunctionDetails(functionUri) {
        this.setState({
            moduleId: null,
            type: null,
            functionUri: functionUri,
            formVisible: true
        });
    };

    deleteModule(moduleId) {
        Actions.deleteModule(this._getScript(), moduleId);
    }

    addNode(id, label, type) {
        this.state.view.startTransaction('loadgraph');
        this.state.view.addNode(id,
            this.state.library[type] !== undefined ? type : 'basic',
            {
                x: this.state.coordinates === undefined ? 0 : this.state.coordinates["x"],
                y: this.state.coordinates === undefined ? 0 : this.state.coordinates["y"],
                label: label,
                types: [type]
            });
        this.state.view.endTransaction('loadGraph');
    };

    duplicateTab() {
        if (Object.keys(that.props.location.query).length === 0) {
            let localStorageId = Math.random().toString(36).slice(2);
            that.state.view.toJSON = undefined;
            localStorage.setItem(localStorageId, JSON.stringify(that.state.view));
            window.open(location.href + "?q=" + localStorageId, '_blank');
        }
        else
            window.open(location.href, '_blank');
    }

    closeModal() {
        this.setState({modalVisible: false});
    };

    onNotificationReceived() {
        this.setState({modalVisible: true});
    };

    // TODO Find some actually usable layouts
    _renderView(algorithm) {

        this.setState({viewLaidOut: false});

        let elkGraph = {
            id: that._getScript(),
            children: [],
            edges: [],
            x: 0,
            y: 0
        };

        const labelLengths = this.state.view.nodes.map(n =>
            n === undefined ? 0 :
                n.metadata === undefined ? 0 :
                    n.metadata.label === undefined ? 0 :
                        n.metadata.label.length);
        let maxLabelLength = 0;
        for (let l in labelLengths)
            maxLabelLength = (parseInt(labelLengths[l]) > maxLabelLength ? parseInt(labelLengths[l]) : maxLabelLength)
        this.state.view.nodes.map(n => {
            elkGraph.children.push({
                id: n.id,
                height: 100,
                width: maxLabelLength * 6.5
            });
        });

        this.state.view.edges.map(e => {
            elkGraph.edges.push({
                source: e.from.node,
                target: e.to.node,
                id: e.metadata.id === undefined ? e.from.node + "->" + e.to.node : e.metadata.id
            });
        });

        elkGraph.properties = {'elk.algorithm': algorithm, 'elk.direction': direction};

        // The graph editor
        let editor = document.getElementById('editor');

        editor.className = "the-graph-light";

        function renderEditor() {

            let props = {
                readonly: false,
                height: document.getElementById('editor').offsetHeight,
                width: document.getElementById('editor').offsetWidth,
                graph: that.state.view,
                library: that.state.library,
                menus: that.state.contextMenus
            };
            let editor = document.getElementById('editor');
            editor.width = props.width;
            editor.height = props.height;
            let element = React.createElement(TheGraph.App, props);

            // Render the numbnail
            let thumb = document.getElementById('thumb');
            let properties = TheGraph.thumb.styleFromTheme('light');
            properties.width = thumb.width;
            properties.height = thumb.height;
            properties.nodeSize = 60;
            properties.lineWidth = 1;
            let context = thumb.getContext("2d");
            TheGraph.thumb.render(context, that.state.view, properties);

            ReactDOM.render(element, editor);
        }

        that.state.view.on('endTransaction', renderEditor); // graph changed
        window.addEventListener("resize", renderEditor);

        if (algorithm === "none") {
            that.state.view.startTransaction('loadgraph');
            that.state.view.endTransaction('loadgraph');
            that.setState({viewLaidOut: true});
        }
        else {
            let elk = new ELK();
            let options = {
                'org.eclipse.elk.layered.crossingMinimization.strategy': 'INTERACTIVE',
            };
            elk.layout(elkGraph, options).then((g) => {
                    that.state.view.startTransaction('loadgraph');
                    for (let i = 0; i < g["children"].length; i++) {
                        that.state.view.nodes[i].metadata.x = g["children"][i].x + 100;
                        that.state.view.nodes[i].metadata.y = g["children"][i].y + 100;
                    }
                    that.state.view.endTransaction('loadgraph');
                    that.setState({viewLaidOut: true});
                },
                (err) => {
                    console.log(err);
                });
        }
    };
}

window.onresize = () => {
    const svg = document.getElementsByTagName("svg")[0];
    const canvas = document.getElementsByTagName("canvas")[0];
    const content = document.getElementById("content");
    const width = content.clientWidth - 2 * parseInt(window.getComputedStyle(content, null).getPropertyValue("padding-right"));
    const height = content.clientHeight;
    svg.style.width = width;
    svg.style.height = height;
    canvas.style.width = width;
    canvas.style.height = height;
};

export default injectIntl(I18nWrapper(Messager(DragDropContext(HTML5Backend)(ViewController))));