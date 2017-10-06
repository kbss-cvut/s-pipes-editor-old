'use strict';

const Reflux = require('reflux');
const jsonld = require('jsonld');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');
const Logger = require('../utils/Logger');

const options = {};

const FormGenStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadFormOptions, this.onLoadFormOptions);
    },

    onLoadFormOptions: function (id, query) {
        if (options[id] && options[id].length !== 0) {
            this.trigger(id, options[id]);
            return;
        }
        Ajax.get('rest/formGen/possibleValues?query=' + encodeURIComponent(query)).end(function (data) {
            if (data.length > 0) {
                jsonld.frame(data, {}, null, function (err, framed) {
                    options[id] = framed['@graph'];
                    this.trigger(id, options[id]);
                }.bind(this));
            } else {
                Logger.warn('No data received when loading options using query' + query + '.');
                this.trigger(id, this.getOptions(id));
            }

        }.bind(this), function () {
            this.trigger(id, this.getOptions(id));
        }.bind(this));
    },

    getOptions: function (id) {
        return options[id] ? options[id] : [];
    }
});

module.exports = FormGenStore;
