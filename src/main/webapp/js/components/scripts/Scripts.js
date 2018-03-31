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

class Scripts extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            scripts: null,
            loading: true
        }
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
                {this.state.scripts.map((s) => {
                    return <NavItem key={s} onSelect={this._showView}>{s}</NavItem>;
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
        this.setState({loading: false, scripts: scripts});
    }

    _showView() {
        let options = {state: {script: this.children}};
        Routing.transitionTo(Routes.views, options);
    }
}

export default injectIntl(I18nWrapper(Messager(Scripts)));