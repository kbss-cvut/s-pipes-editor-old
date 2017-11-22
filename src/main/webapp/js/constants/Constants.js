'use strict';

import Routes from '../utils/Routes';

module.exports = {
    APP_NAME: 'SPEd',
    HOME_ROUTE: Routes.dashboard,

    /**
     * Types of message published by the MessageStore
     */
    MESSAGE_TYPE: {
        SUCCESS: 'success',
        INFO: 'info',
        WARNING: 'warning',
        ERROR: 'danger'
    },

    /**
     * Sorting glyph icons
     */
    SORTING: {
        NO: {glyph: 'sort', title: 'sort.no'},
        ASC: {glyph: 'chevron-up', title: 'sort.asc'},
        DESC: {glyph: 'chevron-down', title: 'sort.desc'}
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    DASHBOARDS: {
        MAIN: {
            id: 'main',
            title: 'dashboard.welcome'
        },
        CREATE_REPORT: {
            id: 'createReport',
            title: 'dashboard.create-tile'
        },
        IMPORT_REPORT: {
            id: 'importReport',
            title: 'dashboard.create-import-tile'
        }
    },

    /**
     * Navigation between dashboards. Key is the current dashboard, value is the target to navigate to on goBack
     */
    DASHBOARD_GO_BACK: {
        'main': 'main',
        'createReport': 'main',
        'importReport': 'createReport'
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds

    // Maximum number of columns supported by Bootstrap
    COLUMN_COUNT: 12,

    // Maximum input value length, for which input of type text should be displayed
    INPUT_LENGTH_THRESHOLD: 70,

    PASSWORD_LENGTH: 4,

    RECORD_REQUIRED_FIELDS: ['localName']
};