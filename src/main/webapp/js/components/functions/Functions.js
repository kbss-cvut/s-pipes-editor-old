import * as React from 'react';
import Actions from '../../actions/Actions';
import FunctionStore from '../../stores/FunctionStore';
import Mask from "../Mask";
import Messager from "../wrapper/Messager";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import {Accordion, Button, Modal, Panel} from 'react-bootstrap';
import Record from "../record/Record";
import * as RouterStore from "../../stores/RouterStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import * as EntityFactory from "../../utils/EntityFactory";

const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";
const FUNCTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-dto";
const FUNCTION_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-uri";
const FUNCTION_NAME = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-local-name";

let record;

class Functions extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            formVisible: false,
            functions: null,
            functionUri: null,
            loading: true,
            record: EntityFactory.initNewRecord()
        };
    }

    componentWillMount() {
        Actions.listAllFunctions();
    }

    render() {
        if (this.state.loading)
            return <Mask/>;
        if (this.state.functions == null)
            return <p>No functions</p>;
        let handlers = {
            onCancel: this._onCancel,
            onChange: this._onChange,
            onSave: this._mergeForm
        };
        record = <Record
            ref={(c) => this.recordComponent = c}
            handlers={handlers}
            record={this.state.record}
            script={this.state.script}
            functionUri={this.state.functionUri}
            loading={this.state.loading}/>;
        return <div>
            <Accordion>
                {this.state.functions.map(f =>
                    <Panel header={f[SCRIPT_PATH]} key={f[SCRIPT_PATH]} eventKey={f[SCRIPT_PATH]}>
                        {f[FUNCTION_DTO]["@list"].map(fn =>
                            <Button key={fn[FUNCTION_URI]} onClick={() => this.setState({
                                functionUri: fn[FUNCTION_URI],
                                script: f[SCRIPT_PATH],
                                formVisible: true
                            })}>
                                {fn[FUNCTION_NAME]}
                            </Button>)}
                    </Panel>
                )}
            </Accordion>
            <Modal dialogClassName="form-modal" show={this.state.formVisible}>
                <Modal.Body>
                    {record}
                </Modal.Body>
            </Modal>
        </div>;
    }

    componentDidMount() {
        this.unsubscribeView = FunctionStore.listen(f => this._functionsLoaded(f.functions));
    }

    _functionsLoaded(functions) {
        if (functions == null) {
            this.setState({loading: false});
            return;
        }
        this.setState({loading: false, functions: functions});
    }

    _mergeForm = () => {
        const formData = this.recordComponent.refs.wrappedInstance.getWrappedComponent().getFormData();
        Actions.saveFunctionForm(this.state.script, this.state.functionUri, formData);
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
}

export default injectIntl(I18nWrapper(Messager(Functions)));