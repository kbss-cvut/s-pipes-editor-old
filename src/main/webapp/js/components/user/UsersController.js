'use strict';

import React from 'react';
import Actions from '../../actions/Actions';
import Authentication from '../../utils/Authentication';
import Routes from '../../utils/Routes';
import Routing from '../../utils/Routing';
import Users from './Users';
import UserStore from '../../stores/UserStore';

export default class UsersController extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: UserStore.getAllUsers()
        };
    }

    componentDidMount() {
        Actions.loadAllUsers();
        this.unsubscribe = UserStore.listen(this._onUsersLoaded);
    }

    _onUsersLoaded = (data) => {
        if (data.action !== Actions.loadAllUsers) {
            return;
        }
        this.setState({users: data.data});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onEditUser = (user) => {
        Routing.transitionTo(Routes.editUser, {
            params: {username: user.username},
            handlers: {
                onCancel: Routes.users
            }
        });
    };

    _onAddUser = () => {
        Routing.transitionTo(Routes.createUser, {
            handlers: {
                onSuccess: Routes.users,
                onCancel: Routes.users
            }
        });
    };

    _onDeleteUser = (user) => {
        Actions.deleteUser(user, Actions.loadAllUsers);
    };

    render() {
        if (!Authentication.isAdmin()) {
            return null;
        }
        let handlers = {
            onEdit: this._onEditUser,
            onCreate: this._onAddUser,
            onDelete: this._onDeleteUser
        };
        return <Users users={this.state.users} handlers={handlers}/>;
    }
}
