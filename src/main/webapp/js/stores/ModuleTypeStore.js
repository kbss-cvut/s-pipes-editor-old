'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const SCRIPT_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/script-dto";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";

//todo Rewrite to "extends" form
const ModuleTypeStore = Reflux.createStore({
    listenables: [Actions],

    onLoadAllModuleTypes: function (script) {
        const request = {};
        request["@type"] = SCRIPT_DTO;
        request[SCRIPT_PATH] = script;
        Ajax.post('rest/scripts/moduleTypes', request).end(
            (data) => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            },
            () => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            });
    }
});

module.exports = ModuleTypeStore;