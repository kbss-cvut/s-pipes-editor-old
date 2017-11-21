'use strict';

import React from 'react';
import {Alert, Button} from 'react-bootstrap';
import injectIntl from '../utils/injectIntl';
import {FormattedMessage} from 'react-intl';

import Routing from '../utils/Routing';
import I18nMixin from '../i18n/I18nMixin';

/**
 * Shows alert with message informing that a resource could not be found.
 *
 * Closing the alert transitions the user to the application's home.
 */
let ResourceNotFound = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        resource: React.PropTypes.string.isRequired,
        identifier: React.PropTypes.object
    },

    onClose: function () {
        Routing.transitionToHome();
    },

    render: function () {
        let text;
        if (this.props.identifier) {
            text = <FormattedMessage id='notfound.msg-with-id'
                                     values={{resource: this.props.resource, identifier: this.props.identifier}}/>;
        } else {
            text = <FormattedMessage id='notfound.msg' values={{resource: this.props.resource}}/>;
        }
        return (<Alert bsStyle='danger' onDismiss={this.onClose}>
            <h4>{this.i18n('notfound.title')}</h4>

            <p>{text}</p>

            <p>
                <Button onClick={this.onClose}>{this.i18n('close')}</Button>
            </p>
        </Alert>);
    }
});

module.exports = injectIntl(ResourceNotFound);
