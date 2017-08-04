'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Authentication = require('../utils/Authentication');
var Utils = require('../utils/Utils');

//todo Rewrite to "extends" form
var ModuleTypeStore = Reflux.createStore({
    listenables: [Actions],

    onLoadView: function () {
        Ajax.get('rest/json/new').end(
            (data) => {
                this.trigger({action: Actions.loadView, data: data});
            },
            () => {
                this.trigger({action: Actions.loadView, data: data});
            });
    }
});

module.exports = ModuleTypeStore;