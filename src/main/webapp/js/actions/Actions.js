'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadAllUsers', 'loadCurrentUser', 'loadUser', 'createUser', 'updateUser', 'deleteUser',
    'loadAllRecords', 'loadRecord', 'createRecord', 'updateRecord', 'deleteRecord',
    'loadFormOptions',
    'loadAllModuleTypes',
    'loadView'
]);

module.exports = Actions;
