var React = require('react');
var ReactDOM = require('react-dom');
var Router = require('react-router').Router;
var Route = require('react-router').Route;
var IndexRoute = require('react-router').IndexRoute;


import { Button } from 'react-bootstrap';
import { Configuration, WizardGenerator } from "semforms";
import I18nStore from "./stores/I18nStore";
import WizardStore from "./stores/WizardStore";
import RecordController from "./components/record/RecordController";

var addLocaleData = require('react-intl').addLocaleData;
var intlData = require('./i18n/en');
var IntlProvider = require('react-intl').IntlProvider;

var FoldableGraph = require("./components/FoldableGraph");
var Panel = require("./components/PanelComp");
var Popup = require("./components/PopupComp");
var ContextMenu = require("./components/ContextMenuComp");

Configuration.wizardStore = WizardStore;
Configuration.intl = I18nStore.getIntl();

I18nStore.setMessages(intlData.messages);

var callback2  = (prop)=>{
    console.log("prop: ", prop);
};

var wiz = WizardGenerator.createDefaultWizard(null, null, callback2);

var App = React.createClass({
    render: function () {
        return (
            <IntlProvider {...intlData}>
                <div>
                    <FoldableGraph/>
                    <Panel/>
                    <Popup/>
                    <script type="text/javascript" src="popup.js"></script>
                    <ContextMenu/>
                    {/* <div className="RecordContr"><RecordController/></div> */}
                </div>
            </IntlProvider>
        );
    }
});

ReactDOM.render(<App />, document.getElementById('root'));
