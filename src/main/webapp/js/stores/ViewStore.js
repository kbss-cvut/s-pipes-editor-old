'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');

//todo Rewrite to "extends" form
const ViewStore = Reflux.createStore({
    listenables: [Actions],

    onLoadView: function (script) {
        Ajax.get('rest/views/' + script + "/new").end(
            (data) => {
                this.trigger({action: Actions.loadView, data: data});
            },
            () => {
                this.trigger({action: Actions.loadView, data: data});
            });
    }
});

module.exports = ViewStore;