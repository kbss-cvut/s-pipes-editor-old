'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const SCRIPT_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/script-dto";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";

//todo Rewrite to "extends" form
const ViewStore = Reflux.createStore({
    listenables: [Actions],

    onLoadView: function (script) {
        const request = {};
        request["@type"] = SCRIPT_DTO;
        request[SCRIPT_PATH] = script;
        Ajax.post('rest/views/new', request).end(
            (data) => {
                this.trigger({action: Actions.loadView, data: data});
            },
            (error) => {
                this.trigger({action: Actions.loadView, data: error});
            });
    }
});

module.exports = ViewStore;