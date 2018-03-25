'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const NEXT_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/next-dto";
const FROM = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-source-uri";
const TO = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-target-uri";

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
        request["@type"] = NEXT_DTO;
        request[FROM] = from;
        request[TO] = to;
        Ajax.post('rest/scripts/' + script + "/modules/dependency", request).end();
    }
});

module.exports = ScriptStore;