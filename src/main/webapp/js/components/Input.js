/**
 * @jsx
 */

'use strict';

import React from 'react';

import {Input as BootstrapInput} from 'react-bootstrap';

let Input = React.createClass({

    focus: function() {
        this.refs.input.getInputDOMNode().focus();
    },

    render: function () {
        if (this.props.type === 'textarea') {
            return <BootstrapInput ref='input' bsSize='small' style={{height: 'auto'}} {...this.props}/>;
        } else {
            return <BootstrapInput ref='input' bsSize='small' {...this.props}/>;
        }
    }
});

module.exports = Input;
