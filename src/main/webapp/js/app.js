/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var I18nStore = require('./stores/I18nStore');
var addLocaleData = require('react-intl').addLocaleData;

var intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    var lang = navigator.language;
    // if (lang && lang === 'cs' || lang === 'cs-CZ' || lang === 'sk' || lang === 'sk-SK') {
    //     intlData = require('./i18n/cs');
    // } else {
    //     intlData = require('./i18n/en');
    // }
    intlData = require('./i18n/en');
}

selectLocalization();
I18nStore.setMessages(intlData.messages);

// Have the imports here, so that the I18nStore is initialized before any of the components which might need it
var React = require('react');
var ReactDOM = require('react-dom');
var Router = require('react-router').Router;
var Route = require('react-router').Route;
var IndexRoute = require('react-router').IndexRoute;
var IntlProvider = require('react-intl').IntlProvider;

var history = require('./utils/Routing').history;
var Routes = require('./utils/Routes');
var Actions = require('./actions/Actions');

var Login = require('./components/login/Login');
var MainView = require('./components/MainView');
var DashboardController = require('./components/dashboard/DashboardController');
var RecordController = require('./components/record/RecordController').default;
var RecordsController = require('./components/record/RecordsController').default;
var UsersController = require('./components/user/UsersController').default;
var UserController = require('./components/user/UserController').default;
var RoutingRules = require('./utils/RoutingRules');

function onRouteEnter() {
    RoutingRules.execute(this.path);
}

// Wrapping router in a React component to allow Intl to initialize
var App = React.createClass({
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
                </Route>
            </Router>
        </IntlProvider>;
    }
});

Actions.loadCurrentUser();

// Pass intl data to the top-level component
ReactDOM.render(<App/>, document.getElementById('content'));