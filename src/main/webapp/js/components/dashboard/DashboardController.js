'use strict';

var React = require('react');
var Reflux = require('reflux');

var injectIntl = require('../../utils/injectIntl');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var UserStore = require('../../stores/UserStore');
var RouterStore = require('../../stores/RouterStore');
var Dashboard = require('./Dashboard');
var I18nMixin = require('.././I18nMixin');

var DashboardController = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded'),
        I18nMixin
    ],
    getInitialState: function () {
        return {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : ''
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    createEmptyReport: function (reportType) {
        Routing.transitionTo(Routes.createReport, {
            handlers: {
                onSuccess: Routes.reports,
                onCancel: Routes.dashboard
            },
            payload: {
                reportType: reportType
            }
        });
    },

    _showUsers: function () {
        Routing.transitionTo(Routes.users);
    },

    _showClinics: function () {
        Routing.transitionTo(Routes.clinics);
    },

    _showRecords: function () {
        Routing.transitionTo(Routes.records);
    },

    _createRecord: function () {
        Routing.transitionTo(Routes.createRecord, {
            handlers: {
                onSuccess: Routes.records,
                onCancel: Routes.dashboard
            }
        });
    },


    render: function () {
        var handlers = {
            showUsers: this._showUsers,
            showClinics: this._showClinics,
            showRecords: this._showRecords,
            createRecord: this._createRecord
        };
        return <div>
            <Dashboard userFirstName={this.state.firstName} dashboard={this._resolveDashboard()} handlers={handlers}/>
        </div>;
    },

    _resolveDashboard: function () {
        var payload = RouterStore.getTransitionPayload(Routes.dashboard.name);
        RouterStore.setTransitionPayload(Routes.dashboard.name, null);
        return payload ? payload.dashboard : null;
    }
});

module.exports = injectIntl(DashboardController);
