'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const DEPENDENCY_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/dependency-dto";
const FROM = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-uri";
const TO = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-target-module-uri";
const ABSOLUTE_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-absolute-path";
const SCRIPT_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/script-dto";

//todo Rewrite to "extends" form
const ScriptStore = Reflux.createStore({
    listenables: [Actions],

    onListScripts: function () {
        Ajax.get('rest/scripts/tree').end(
            (scripts) => {
                this.trigger({action: Actions.listScripts, scripts: scripts});
            },
            () => {
                this.trigger({action: Actions.listScripts, scripts: null});
            });
    },

    onListFunctions: function (script) {
        const request = {};
        request["@type"] = SCRIPT_DTO;
        request[ABSOLUTE_PATH] = script;
        Ajax.post('rest/scripts/functions', request).end(
            (data) => {
                this.trigger({action: Actions.listFunctions, data: data});
            },
            (error) => {
                this.trigger({action: Actions.listFunctions, data: error});
            });
    },

    onCreateDependency: function (script, from, to) {
        const request = {};
        request["@type"] = DEPENDENCY_DTO;
        request[ABSOLUTE_PATH] = script;
        request[FROM] = from;
        request[TO] = to;
        Ajax.post("rest/scripts/modules/dependency", request).end();
    },

    onDeleteDependency: function (script, from, to) {
        const request = {};
        request["@type"] = DEPENDENCY_DTO;
        request[ABSOLUTE_PATH] = script;
        request[FROM] = from;
        request[TO] = to;
        Ajax.post("rest/scripts/modules/dependencies/delete", request).end();
    }
});

module.exports = ScriptStore;