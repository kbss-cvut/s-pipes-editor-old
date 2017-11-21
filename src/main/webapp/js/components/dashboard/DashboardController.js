'use strict';

import React from 'react';
import Reflux from 'reflux';

import injectIntl from '../../utils/injectIntl';

import Routing from '../../utils/Routing';
import Routes from '../../utils/Routes';
import UserStore from '../../stores/UserStore';
import RouterStore from '../../stores/RouterStore';
import Dashboard from './Dashboard';
import I18nMixin from '../../i18n/I18nMixin';

let DashboardController = React.createClass({
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

    _showViews: function () {
        Routing.transitionTo(Routes.views);
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
        let handlers = {
            showUsers: this._showUsers,
            showRecords: this._showRecords,
            createRecord: this._createRecord,
            showViews: this._showViews
        };
        return <div>
            <Dashboard userFirstName={this.state.firstName} dashboard={this._resolveDashboard()} handlers={handlers}/>
        </div>;
    },

    _resolveDashboard: function () {
        let payload = RouterStore.getTransitionPayload(Routes.dashboard.name);
        RouterStore.setTransitionPayload(Routes.dashboard.name, null);
        return payload ? payload.dashboard : null;
    }
});

module.exports = injectIntl(DashboardController);
