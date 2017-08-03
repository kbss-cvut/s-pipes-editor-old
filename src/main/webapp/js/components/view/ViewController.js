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
import * as Ajax from "../../utils/Ajax";

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
                                    key={m["@id"]}>
                                {m["@id"].toString().split("/").reverse()[0]}
                            </Button>
                        </OverlayTrigger>
                    })}
                </ButtonGroup>
                <div id="editor">
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
            this._renderView();
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

    _renderView() {
        "use strict";
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

        function renderEditor() {
            var props = {
                readonly: false,
                height: document.getElementById('editor').offsetHeight,
                width: document.getElementById('editor').offsetWidth,
                graph: graph,
                library: library,
            };
            //console.log('render', props);
            var editor = document.getElementById('editor');
            editor.width = props.width;
            editor.height = props.height;
            var element = React.createElement(TheGraph.App, props);
            ReactDOM.render(element, editor);
            that.setState({viewLoaded: true});
        }

        graph.on('endTransaction', renderEditor); // graph changed
        window.addEventListener("resize", renderEditor);

        // Load graph
        Ajax.get('rest/json/new').end(
            (data) => {
                graph.startTransaction('loadgraph');
                data["children"].map((m) => {
                    // TODO layout
                    let metadata = {
                        label: m["id"],
                        x: Math.round(Math.random() * 800),
                        y: Math.round(Math.random() * 600)
                    };
                    graph.addNode(m["id"], 'basic', metadata);
                });
                data["edges"].map((e) => {
                    graph.addEdge(e["source"], 'out', e["target"], 'in', undefined);
                });
                graph.endTransaction('loadgraph');
                graph.nodes.map((n) => {
                    document.querySelectorAll('[title="' + n.id + '"]')[0].addEventListener('dblclick', function (e) {
                        e.stopPropagation();
                        that.openForm();
                    });
                });
                that.setState({viewLoaded: true});
            },
            () => {
                console.log("Shit");
            });
    }
}

export default injectIntl(I18nWrapper(Messager(ViewController)));