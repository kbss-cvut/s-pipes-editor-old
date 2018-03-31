'use strict';
import * as RouterStore from '../stores/RouterStore';
import {hashHistory} from 'react-router';
import Constants from '../constants/Constants';
import RoutingRules from './RoutingRules';

const Routing = {
    history: hashHistory,

    originalTarget: null,

    /**
     * Transitions to the specified route
     * @param route Route object
     * @param options Transition options, can specify path parameters, query parameters, payload and view handlers.
     */
    transitionTo: function (route, options) {
        let path = route.path;
        if (!options) {
            options = {};
        }
        if (options.params) {
            path = this.setPathParams(path, options.params);
        }
        RouterStore.setTransitionPayload(route.name, options.payload);
        RouterStore.setViewHandlers(route.name, options.handlers);
        RoutingRules.execute(route.name);
        this.history.push({
            pathname: path,
            search: options.query,
            state: options.state
        });
    },

    setPathParams: function (path, params) {
        for (let paramName in params) {
            if (params.hasOwnProperty(paramName)) {
                path = path.replace(':' + paramName, params[paramName]);
            }
        }
        return path;
    },

    transitionToHome: function (options) {
        this.transitionTo(Constants.HOME_ROUTE, options);
    },

    saveOriginalTarget: function (route) {
        if (!route || route === '') {
            return;
        }
        this.originalTarget = route;
    },

    transitionToOriginalTarget: function () {
        if (this.originalTarget) {
            this.transitionTo(this.originalTarget);
        } else {
            this.transitionTo(Constants.HOME_ROUTE);
        }
    }
};

module.exports = Routing;