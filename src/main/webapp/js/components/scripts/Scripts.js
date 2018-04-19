import * as React from 'react';
import Actions from '../../actions/Actions';
import ScriptStore from '../../stores/ScriptStore';
import {Nav, NavItem} from 'react-bootstrap';
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Messager from "../wrapper/Messager";
import Mask from "../Mask";
import Routing from "../../utils/Routing";
import Routes from "../../utils/Routes";

const ABSOLUTE_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-absolute-path";
const RELATIVE_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";

let parent;

class Scripts extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            scripts: null,
            loading: true
        };
        parent = this;
    }

    componentWillMount() {
        Actions.listScripts();
    }

    render() {
        if (this.state.loading)
            return <Mask/>;
        if (this.state.scripts == null)
            return <p>No scripts found</p>;
        return (
            <Nav bsStyle="pills">
                {Object.keys(this.state.scripts).map(s => {
                    return <NavItem key={this.state.scripts[s][ABSOLUTE_PATH]}
                                    onSelect={this._showView}>{this.state.scripts[s][RELATIVE_PATH]}</NavItem>;
                })}
            </Nav>)
    }

    componentDidMount() {
        this.unsubscribeView = ScriptStore.listen((s) => this._scriptsLoaded(s.scripts));
    }

    componentWillUnmount() {
        this.unsubscribeView();
    }

    _scriptsLoaded(scripts) {
        if (scripts == null) {
            this.setState({loading: false});
            return;
        }
        Object.keys(scripts).forEach(k => {
            const newkey = scripts[k][RELATIVE_PATH];
            scripts[newkey] = scripts[k];
            delete scripts[k];
        });
        this.setState({loading: false, scripts: scripts});
    }

    _showView() {
        const options = {state: {script: parent.state.scripts[this.children][ABSOLUTE_PATH]}};
        Routing.transitionTo(Routes.views, options);
    }
}

export default injectIntl(I18nWrapper(Messager(Scripts)));