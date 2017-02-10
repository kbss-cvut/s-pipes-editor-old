'use strict';

import React from 'react';
import {Button, Panel} from 'react-bootstrap';

import injectIntl from '../../utils/injectIntl';
import I18nWrapper from '.././I18nWrapper';
import Mask from '../Mask';
import UserTable from './UserTable';

class Users extends React.Component {
    static propTypes = {
        users: React.PropTypes.array,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
    }

    render() {
        var users = this.props.users;
        if (users === null) {
            return <Mask text={this.i18n('please-wait')}/>;
        }
        return <Panel header={this.i18n('users.panel-title')} bsStyle='primary'>
            <UserTable {...this.props}/>
            <div>
                <Button bsStyle='primary'
                        onClick={this.props.handlers.onCreate}>{this.i18n('users.create-user')}</Button>
            </div>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(Users));
