/**
 Main entry point for the ReactJS frontend
 */

'use strict';

import I18nStore from './stores/I18nStore';
// Have the imports here, so that the I18nStore is initialized before any of the components which might need it
import React from 'react';
import ReactDOM from 'react-dom';
import {IndexRoute, Route, Router} from 'react-router';
import {addLocaleData as addLocaleData, IntlProvider} from 'react-intl';
import {history} from './utils/Routing';
import Routes from './utils/Routes';
import MainView from './components/MainView';
import DashboardController from './components/dashboard/DashboardController';
import RoutingRules from './utils/RoutingRules';
import ViewController from './components/view/ViewController';
import Scripts from "./components/scripts/Scripts";

let intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    intlData = require('./i18n/en');
}

selectLocalization();
I18nStore.setMessages(intlData.messages);

function onRouteEnter() {
    RoutingRules.execute(this.path);
}

// Wrapping router in a React component to allow Intl to initialize
const App = React.createClass({
    render: function () {
        return <IntlProvider {...intlData}>
            <Router history={history}>
                <Route path='/' component={MainView}>
                    <IndexRoute component={DashboardController}/>
                    <Route path={Routes.dashboard.path} onEnter={onRouteEnter} component={DashboardController}/>
                    <Route path={Routes.views.path} onEnter={onRouteEnter} component={ViewController}/>
                    <Route path={Routes.scripts.path} onEnter={onRouteEnter} component={Scripts}/>
                </Route>
            </Router>
        </IntlProvider>;
    }
});

// Pass intl data to the top-level component
ReactDOM.render(<App/>, document.getElementById('content'));