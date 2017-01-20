'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var currentUser = null;
var loaded = false;
var users = null;


var UserStore = Reflux.createStore({
    listenables: [Actions],

    onLoadCurrentUser: function () {
        if (currentUser === null) {
            Ajax.get('rest/users/current').end(UserStore._userLoaded);
        } else {
            this.trigger({action: Actions.loadCurrentUser, data: currentUser});
        }
    },

    _userLoaded: function (user) {
        currentUser = user;
        loaded = true;
        this.trigger({action: Actions.loadCurrentUser, data: this.getCurrentUser()});
    },

    getCurrentUser: function () {
        return currentUser;
    },

    isLoaded: function () {
        return loaded;
    },

    onLoadAllUsers: function () {
        Ajax.get('rest/users').end((data) => {
            users = data;
            this.trigger({action: Actions.loadAllUsers, data: users});
        });
    },

    onLoadClinicMembers: function (clinicKey) {
        Ajax.get('rest/users?clinic=' + clinicKey).end((data) => {
            this.trigger({action: Actions.loadClinicMembers, clinicKey: clinicKey, data: data});
        });
    },

    onLoadUser: function (username) {
        Ajax.get('rest/users/' + username).end((data) => {
            this.trigger({action: Actions.loadUser, data: data});
        });
    },

    onCreateUser: function (user, onSuccess, onError) {
        Ajax.post('rest/users').send(user).end((data, resp) => {
            if (onSuccess) {
                var username = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(username);
            }
            Actions.loadAllUsers();
        }, onError);
    },

    onUpdateUser: function (user, onSuccess, onError) {
        Ajax.put('rest/users/' + user.username).send(user).end(onSuccess, onError);
    },

    onDeleteUser: function (user, onSuccess, onError) {
        Ajax.del('rest/users/' + user.username).end(onSuccess, onError);
    },

    getAllUsers: function () {
        return users;
    }
});

module.exports = UserStore;
