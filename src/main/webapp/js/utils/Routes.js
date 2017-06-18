'use strict';

var Routes = {
    login: {name: 'login', path: 'login'},
    dashboard: {name: 'dashboard', path: 'dashboard'},
    users: {name: 'users', path: 'users'},
    createUser: {name: 'createUser', path: 'users/create'},
    editUser: {name: 'editUser', path: 'users/:username'},
    records: {name: 'records', path: 'records'},
    createRecord: {name: 'createRecord', path: 'records/create'},
    editRecord: {name: 'editRecord', path: 'records/:key'}
};

module.exports = Routes;
