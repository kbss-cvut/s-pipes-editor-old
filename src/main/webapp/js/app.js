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
import Login from './components/login/Login';
import MainView from './components/MainView';
import DashboardController from './components/dashboard/DashboardController';
import {RecordController} from './components/record/RecordController';
import {UserController} from './components/user/UserController';
import RoutingRules from './utils/RoutingRules';
import {default as RecordsController} from './components/record/RecordsController';
import {default as ViewController} from './components/view/ViewController';
import {default as UsersController} from './components/user/UsersController';

let intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    const lang = navigator.language;
    // if (lang && lang === 'cs' || lang === 'cs-CZ' || lang === 'sk' || lang === 'sk-SK') {
    //     intlData = require('./i18n/cs');
    // } else {
    //     intlData = require('./i18n/en');
    // }
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
                    <Route path={Routes.login.path} onEnter={onRouteEnter} component={Login}/>
                    <Route path={Routes.dashboard.path} onEnter={onRouteEnter} component={DashboardController}/>
                    <Route path={Routes.users.path} onEnter={onRouteEnter} component={UsersController}/>
                    <Route path={Routes.createUser.path} onEnter={onRouteEnter} component={UserController}/>
                    <Route path={Routes.editUser.path} onEnter={onRouteEnter} component={UserController}/>
                    <Route path={Routes.records.path} onEnter={onRouteEnter} component={RecordsController}/>
                    <Route path={Routes.createRecord.path} onEnter={onRouteEnter} component={RecordController}/>
                    <Route path={Routes.editRecord.path} onEnter={onRouteEnter} component={RecordController}/>
                    <Route path={Routes.views.path} onEnter={onRouteEnter} component={ViewController}/>
                </Route>
            </Router>
        </IntlProvider>;
    }
});

// Actions.loadCurrentUser();

// Pass intl data to the top-level component
ReactDOM.render(<App/>, document.getElementById('content'));