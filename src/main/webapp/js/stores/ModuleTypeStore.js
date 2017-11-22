'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

//todo Rewrite to "extends" form
const ModuleTypeStore = Reflux.createStore({
    listenables: [Actions],

    onLoadAllModuleTypes: function (script) {
        Ajax.get('rest/scripts/' + script + "/moduleTypes").end(
            (data) => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            },
            () => {
                this.trigger({action: Actions.loadAllModuleTypes, data: data});
            });
    }
});

module.exports = ModuleTypeStore;