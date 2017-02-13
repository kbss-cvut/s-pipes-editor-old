'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadAllUsers', 'loadCurrentUser', 'loadUser', 'createUser', 'updateUser', 'deleteUser',
    'loadClinicMembers',

    'loadAllClinics', 'loadClinic', 'createClinic', 'updateClinic', 'deleteClinic', 'loadClinicPatients',

    'loadAllRecords', 'loadRecord', 'createRecord', 'updateRecord', 'deleteRecord',

    'loadFormOptions'
]);

module.exports = Actions;
