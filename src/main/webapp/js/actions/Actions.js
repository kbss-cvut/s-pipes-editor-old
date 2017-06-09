'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadAllUsers', 'loadCurrentUser', 'loadUser', 'createUser', 'updateUser', 'deleteUser',

    'loadFormOptions'
]);

module.exports = Actions;
