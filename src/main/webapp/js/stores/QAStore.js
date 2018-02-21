'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

const QUESTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/question-dto";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-uri";
const MODULE_TYPE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-type-uri";
const ROOT_QUESTION = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-root-question";

//todo Rewrite to "extends" form
const QAStore = Reflux.createStore({
    listenables: [Actions],

    onSaveForm: function (script, module, moduleType, rootQuestion) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_TYPE_URI] = moduleType;
        request[MODULE_URI] = module;
        request[ROOT_QUESTION] = rootQuestion;
        Ajax.post('rest/scripts/' + script + "/forms/answers", request).end();
    }
});

module.exports = QAStore;