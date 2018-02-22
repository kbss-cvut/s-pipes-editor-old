'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const QUESTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/question-dto";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-uri";

//todo Rewrite to "extends" form
const ModuleStore = Reflux.createStore({
    listenables: [Actions],

    onDeleteModule: function (script, module) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_URI] = module;
        Ajax.post('rest/scripts/' + script + "/modules/delete", request).end();
    }
});

module.exports = ModuleStore;