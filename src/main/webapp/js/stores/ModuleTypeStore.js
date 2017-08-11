'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Authentication = require('../utils/Authentication');
var Utils = require('../utils/Utils');

//todo Rewrite to "extends" form
var ModuleTypeStore = Reflux.createStore({
    listenables: [Actions],

    onLoadAllModuleTypes: function (script) {
        Ajax.get('rest/scripts/' + script + "/moduleTypes").end(
            (data) => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            },
            () => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            });
    }
});

module.exports = ModuleTypeStore;