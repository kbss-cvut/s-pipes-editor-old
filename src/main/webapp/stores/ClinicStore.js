'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var ClinicStore = Reflux.createStore({
    listenables: [Actions],

    _clinics: null,

    onLoadAllClinics: function () {
        Ajax.get('rest/clinics').end((data) => {
            this._clinics = data;
            this.trigger({action: Actions.loadAllClinics, data: data});
        });
    },

    getClinics: function () {
        return this._clinics;
    },

    onLoadClinic: function (key) {
        Ajax.get('rest/clinics/' + key).end((data) => {
            this.trigger({action: Actions.loadClinic, data: data});
        });
    },

    onCreateClinic: function (clinic, onSuccess, onError) {
        Ajax.post('rest/clinics').send(clinic).end((data, resp) => {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            Actions.loadAllClinics();
        }, onError);
    },

    onUpdateClinic: function (clinic, onSuccess, onError) {
        Ajax.put('rest/clinics/' + clinic.key).send(clinic).end(onSuccess, onError);
    },

    onDeleteClinic: function (clinic, onSuccess, onError) {
        Ajax.del('rest/clinics/' + clinic.key).end(onSuccess, onError);
    }
});

module.exports = ClinicStore;
