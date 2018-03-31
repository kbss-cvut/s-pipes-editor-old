'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const MODULE_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/module-dto";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-uri";

//todo Rewrite to "extends" form
const ModuleStore = Reflux.createStore({
    listenables: [Actions],

    onDeleteModule: function (script, module) {
        const request = {};
        request["@type"] = MODULE_DTO;
        request[SCRIPT_PATH] = script;
        request[MODULE_URI] = module;
        Ajax.post("rest/scripts/modules/delete", request).end();
    }
});

module.exports = ModuleStore;