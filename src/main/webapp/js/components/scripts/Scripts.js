import * as React from 'react';
import Actions from '../../actions/Actions';
import ScriptStore from '../../stores/ScriptStore';
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Messager from "../wrapper/Messager";
import Mask from "../Mask";
import Routing from "../../utils/Routing";
import Routes from "../../utils/Routes";
import {Treebeard} from 'react-treebeard';

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
        this.onToggle = this.onToggle.bind(this);
    }

    onToggle(node, toggled) {
        if (this.state.cursor)
            this.state.cursor.active = false;
        node.active = true;
        if (node.children)
            node.toggled = toggled;
        else
            this._showView(node.file);
        this.setState({cursor: node});
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
            <div>
                <Treebeard
                    data={this.state.scripts}
                    onToggle={this.onToggle}/>
            </div>
        );
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
        this.setState({loading: false, scripts: scripts});
    }

    _showView(script) {
        Routing.transitionTo(Routes.views, {
            params: {key: script.replace(/\//g, "_")},
            state: {script: script}
        });
    }
}

export default injectIntl(I18nWrapper(Messager(Scripts)));