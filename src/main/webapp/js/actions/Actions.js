'use strict';

import Reflux from 'reflux';

const Actions = Reflux.createActions([
    'loadAllUsers', 'loadCurrentUser', 'loadUser', 'createUser', 'updateUser', 'deleteUser',
    'loadAllRecords', 'loadRecord', 'createRecord', 'updateRecord', 'deleteRecord',
    'loadFormOptions',
    'loadAllModuleTypes',
    'loadView',
    'listScripts',
    'saveForm',
    'deleteModule', 'createDependency', 'deleteDependency'
]);

module.exports = Actions;