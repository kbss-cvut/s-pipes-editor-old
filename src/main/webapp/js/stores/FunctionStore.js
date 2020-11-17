'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

//todo Rewrite to "extends" form
const FunctionStore = Reflux.createStore({
    listenables: [Actions],

    onListAllFunctions: function () {
        Ajax.get('rest/functions').end(
            (functions) => {
                this.trigger({action: Actions.listAllFunctions, functions: functions});
            },
            () => {
                this.trigger({action: Actions.listAllFunctions, functions: null});
            });
    },
});

module.exports = FunctionStore;