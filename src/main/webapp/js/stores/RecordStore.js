'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');
const Authentication = require('../utils/Authentication');
const UserStore = require('./UserStore');
const Utils = require('../utils/Utils');

const RecordStore = Reflux.createStore({
    listenables: [Actions],

    _patients: null,

    onLoadAllRecords: function () {
        const user = UserStore.getCurrentUser();
        let urlSuffix = '';
        if (!user) {
            return;
        }
        if (!Authentication.isAdmin() && user.clinic) {
            urlSuffix = '?clinic=' + user.clinic.key;
        }
        Ajax.get('rest/records' + urlSuffix).end((data) => {
            this._patients = data;
            this.trigger({action: Actions.loadAllRecords, data: data});
        }, () => {
            this._patients = [];
            this.trigger({action: Actions.loadAllRecords, data: data});
        });
    },

    getAllRecords: function () {
        return this._patients;
    },

    onLoadClinicPatients: function (clinicKey) {
        Ajax.get('rest/records?clinic=' + clinicKey).end((data) => {
            this.trigger({action: Actions.loadClinicPatients, clinicKey: clinicKey, data: data});
        });
    },

    onLoadRecord: function (key) {
        Ajax.get('rest/records/' + key).end((data) => {
            this.trigger({action: Actions.loadRecord, data: data});
        });
    },

    onCreateRecord: function (record, onSuccess, onError) {
        Ajax.post('rest/records').send(record).end((data, resp) => {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            Actions.loadAllRecords();
        }, onError);
    },

    onUpdateRecord: function (record, onSuccess, onError) {
        Ajax.put('rest/records/' + record.key).send(record).end(onSuccess, onError);
    },

    onDeleteRecord: function (record, onSuccess, onError) {
        Ajax.del('rest/records/' + record.key).end(onSuccess, onError);
    }
});

module.exports = RecordStore;
