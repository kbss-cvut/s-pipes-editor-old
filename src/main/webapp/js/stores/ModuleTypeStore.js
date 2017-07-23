'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Authentication = require('../utils/Authentication');
var Utils = require('../utils/Utils');

//todo Rewrite to "extends" form
var ModuleTypeStore = Reflux.createStore({
    listenables: [Actions],

    onLoadAllModuleTypes: function () {
        Ajax.get('rest/scripts/12/moduleTypes').end(
            (data) => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            },
            () => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            });
    }
});

module.exports = ModuleTypeStore;