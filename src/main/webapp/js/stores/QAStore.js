'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

//todo Rewrite to "extends" form
const QAStore = Reflux.createStore({
    listenables: [Actions],

    onSaveForm: function (script, module, moduleType, rootQuestion) {
        Ajax.post('rest/scripts/' + script + "/forms/answers", {
            module: module,
            moduleType: moduleType,
            rootQuestion: rootQuestion
        }).end();
    }
});

module.exports = QAStore;