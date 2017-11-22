'use strict';

import React from 'react';
import {Alert, Button, Panel} from 'react-bootstrap';

import injectIntl from '../../utils/injectIntl';
import Input from '../Input';
import Routing from '../../utils/Routing';
import Routes from '../../utils/Routes';
import Authentication from '../../utils/Authentication';
import I18nMixin from '../../i18n/I18nMixin';

import {default as Mask} from '../Mask';


let Login = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        return {
            username: '',
            password: '',
            alertVisible: false,
            mask: false
        }
    },

    componentDidMount: function () {
        this.refs.usernameField.focus();
    },

    onChange: function (e) {
        let state = this.state;
        state[e.target.name] = e.target.value;
        state.alertVisible = false;
        this.setState(state);
    },

    onKeyPress: function (e) {
        if (e.key === 'Enter') {
            this.login();
        }
    },

    onLoginError: function () {
        this.setState({alertVisible: true, mask: false});
    },

    login: function () {
        Authentication.login(this.state.username, this.state.password, this.onLoginError);
        this.setState({mask: true});
    },

    register: function () {
        Routing.transitionTo(Routes.register);
    },


    render: function () {
        let panelCls = this.state.alertVisible ? 'login-panel expanded' : 'login-panel',
            mask = this.state.mask ? (<Mask text={this.i18n('login.progress-mask')}/>) : null;
        return <Panel header={<h3>{this.i18n('login.title')}</h3>} bsStyle='info' className={panelCls}>
            {mask}
            <form className='form-horizontal'>
                {this.renderAlert()}
                <Input type='text' name='username' ref='usernameField' label={this.i18n('login.username')}
                       value={this.state.username}
                       onChange={this.onChange} labelClassName='col-xs-3' onKeyPress={this.onKeyPress}
                       wrapperClassName='col-xs-9'/>
                <Input type='password' name='password' label={this.i18n('login.password')}
                       value={this.state.password}
                       onChange={this.onChange} labelClassName='col-xs-3' onKeyPress={this.onKeyPress}
                       wrapperClassName='col-xs-9'/>

                <div className='col-xs-3'>&nbsp;</div>
                <div className='col-xs-9' style={{padding: '0 0 0 7px'}}>
                    <Button bsStyle='success' bsSize='small' onClick={this.login}
                            disabled={this.state.mask}>{this.i18n('login.submit')}</Button>
                </div>
            </form>
        </Panel>;
    },

    renderAlert: function () {
        return this.state.alertVisible ? (
            <Alert bsStyle='danger' bsSize='small'>
                <div>{this.i18n('login.error')}</div>
            </Alert>
        ) : null;
    }
});

module.exports = injectIntl(Login);
