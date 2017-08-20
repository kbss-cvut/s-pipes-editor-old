'use strict';

let Reflux = require('reflux');

let Actions = require('../actions/Actions');
let Ajax = require('../utils/Ajax');

//todo Rewrite to "extends" form
let ViewStore = Reflux.createStore({
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