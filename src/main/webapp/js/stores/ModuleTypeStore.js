'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');
const Authentication = require('../utils/Authentication');
const Utils = require('../utils/Utils');

//todo Rewrite to "extends" form
const ModuleTypeStore = Reflux.createStore({
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