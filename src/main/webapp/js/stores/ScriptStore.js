'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const DEPENDENCY_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/dependency-dto";
const FROM = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-uri";
const TO = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-target-module-uri";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";

//todo Rewrite to "extends" form
const ScriptStore = Reflux.createStore({
    listenables: [Actions],

    onListScripts: function () {
        Ajax.get('rest/scripts').end(
            (scripts) => {
                this.trigger({action: Actions.listScripts, scripts: scripts});
            },
            () => {
                this.trigger({action: Actions.listScripts, scripts: null});
            });
    },

    onCreateDependency: function (script, from, to) {
        const request = {};
        request["@type"] = DEPENDENCY_DTO;
        request[SCRIPT_PATH] = script;
        request[FROM] = from;
        request[TO] = to;
        Ajax.post("rest/scripts/modules/dependency", request).end();
    },

    onDeleteDependency: function (script, from, to) {
        const request = {};
        request["@type"] = DEPENDENCY_DTO;
        request[SCRIPT_PATH] = script;
        request[FROM] = from;
        request[TO] = to;
        Ajax.post("rest/scripts/modules/dependencies/delete", request).end();
    }
});

module.exports = ScriptStore;