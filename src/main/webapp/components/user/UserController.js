'use strict';

import React from 'react';
import assign from 'object-assign';

import Actions from '../../actions/Actions';
import Authentication from '../../utils/Authentication';
import injectIntl from '../../utils/injectIntl';
import I18nWrapper from '../../i18n/I18nWrapper';
import Messager from '../wrapper/Messager';
import User from './User';
import UserFactory from '../../utils/EntityFactory';
import UserStore from '../../stores/UserStore';
import RouterStore from '../../stores/RouterStore';
import Routes from '../../utils/Routes';
import Routing from '../../utils/Routing';

class UserController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: this._isNew() ? UserFactory.initNewUser() : null,
            loading: false
        };
    }

    _isNew() {
        return !this.props.params.username;
    }

    componentWillMount() {
        if (!this.state.user) {
            Actions.loadUser(this.props.params.username);
            this.setState({loading: true});
        }
        this.unsubscribe = UserStore.listen(this._onUserLoaded);
    }

    _onUserLoaded = (data) => {
        if (data.action !== Actions.loadUser) {
            return;
        }
        this.setState({user: data.data, loading: false});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onSave = () => {
        var user = this.state.user;
        if (user.isNew) {
            delete user.isNew;
            Actions.createUser(user, this._onSaveSuccess, this._onSaveError);
        } else {
            Actions.updateUser(user, this._onSaveSuccess, this._onSaveError);
        }
    };

    _onSaveSuccess = (username) => {
        this.props.showSuccessMessage(this.props.i18n('user.save-success'));
        Actions.loadUser(username ? username : this.props.params.username);
    };

    _onSaveError = (err) => {
        this.props.showErrorMessage(this.props.formatMessage('user.save-error', {error: err.message}));
    };

    _onCancel = () => {
        var handlers = RouterStore.getViewHandlers(Routes.editUser.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Authentication.isAdmin() ? Routes.users : Routes.dashboard);
        }
    };

    _onChange = (change) => {
        var update = assign({}, this.state.user, change);
        this.setState({user: update});
    };

    render() {
        return <User onSave={this._onSave} onCancel={this._onCancel} onChange={this._onChange} user={this.state.user}
                     loading={this.state.loading}/>;
    }
}

export default injectIntl(I18nWrapper(Messager(UserController)));
