/**
 * @jsx
 */

'use strict';

import React from 'react';
import injectIntl from '../utils/injectIntl';

import Input from './Input';
import I18nMixin from '../i18n/I18nMixin';

let Select = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        options: React.PropTypes.array,
        name: React.PropTypes.string,
        title: React.PropTypes.string,
        label: React.PropTypes.string,
        onChange: React.PropTypes.func,
        addDefault: React.PropTypes.bool    // Specifies whether the default '----Select----' option should be added
    },

    focus: function () {
        this.refs.select.focus();
    },

    generateOptions: function () {
        let options = [];
        let len = this.props.options.length;
        for (let i = 0; i < len; i++) {
            let option = this.props.options[i];
            options.push(<option key={'opt_' + option.value} value={option.value}
                                 title={option.title}>{option.label}</option>);
        }
        return options;
    },

    getInputNode: function () {
        return this.refs.select.refs.input;
    },

    render: function () {
        let options = this.generateOptions();
        if (this.props.addDefault) {
            options.unshift(<option key='opt_default' value='' disabled style={{display: 'none'}}>
                {this.i18n('select.default')}
            </option>);
        }
        return (
            <Input ref='select' type='select' name={this.props.name} title={this.props.title} label={this.props.label}
                   value={this.props.value ? this.props.value : ''} onChange={this.props.onChange} {...this.props}>
                {options}
            </Input>
        );
    }
});

module.exports = injectIntl(Select);
