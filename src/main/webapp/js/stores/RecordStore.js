'use strict';

import Reflux from 'reflux';
import Actions from '../actions/Actions';
import Ajax from '../utils/Ajax';
import Utils from '../utils/Utils';

const RecordStore = Reflux.createStore({
    listenables: [Actions],

    onCreateRecord: function (record, onSuccess, onError) {
        Ajax.post('rest/records').send(record).end((data, resp) => {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            Actions.loadAllRecords();
        }, onError);
    },
});

module.exports = RecordStore;