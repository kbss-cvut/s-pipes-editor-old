'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const QUESTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/question-dto";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-uri";
const MODULE_TYPE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-type-uri";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";
const ROOT_QUESTION = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-root-question";

//todo Rewrite to "extends" form
const QAStore = Reflux.createStore({
    listenables: [Actions],

    onSaveModuleForm: function (script, module, moduleType, rootQuestion) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_TYPE_URI] = moduleType;
        request[MODULE_URI] = module;
        request[ROOT_QUESTION] = rootQuestion;
        request[SCRIPT_PATH] = script;
        Ajax.post("rest/scripts/forms/answers", request).end();
    },

    onSaveFunctionForm: function (script, functionUri, rootQuestion) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_URI] = functionUri;
        request[ROOT_QUESTION] = rootQuestion;
        request[SCRIPT_PATH] = script;
        Ajax.post("rest/executions/new", request).end();
    }
});

module.exports = QAStore;