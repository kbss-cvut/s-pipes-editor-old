'use strict';

import Constants from '../constants/Constants';

module.exports = {

    isComplete: function (record) {
        if (!record) {
            return false;
        }
        for (let i = 0, len = Constants.RECORD_REQUIRED_FIELDS.length; i < len; i++) {
            if (!record[Constants.RECORD_REQUIRED_FIELDS[i]]) {
                return false;
            }
        }
        return true;
    }
};