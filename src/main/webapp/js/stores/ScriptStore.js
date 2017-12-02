'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';

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
    }
});

module.exports = ScriptStore;