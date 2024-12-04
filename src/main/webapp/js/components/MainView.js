'use strict';

import React from 'react';
import injectIntl from '../utils/injectIntl';
import I18nMixin from '../i18n/I18nMixin';
import I18nStore from '../stores/I18nStore';

let MainView = React.createClass({
    mixins: [
        I18nMixin
    ],

    componentWillMount: function () {
        I18nStore.setIntl(this.props.intl);
    },

    render: function () {
        return (<div>{this.props.children}</div>);
    },
});

module.exports = injectIntl(MainView);