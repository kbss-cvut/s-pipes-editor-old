'use strict';

import Actions from '../actions/Actions';
import Ajax from './Ajax';
import Routes from './Routes';
import Routing from './Routing';
import Logger from './Logger';
import UserStore from '../stores/UserStore';
import Vocabulary from '../constants/Vocabulary';

const Authentication = {

    login: function (username, password, errorCallback) {
        Ajax.post('j_spring_security_check', null, 'form')
            .send('username=' + username).send('password=' + password)
            .end(function (err, resp) {
                if (err) {
                    errorCallback();
                    return;
                }
                const status = JSON.parse(resp.text);
                if (!status.success || !status.loggedIn) {
                    errorCallback();
                    return;
                }
                Actions.loadCurrentUser();
                Logger.log('User successfully authenticated.');
                Routing.transitionToOriginalTarget();
            }.bind(this));
    },

    logout: function () {
        Ajax.post('j_spring_security_logout').end(function (err) {
            if (err) {
                Logger.error('Logout failed. Status: ' + err.status);
            } else {
                Logger.log('User successfully logged out.');
            }
            Routing.transitionTo(Routes.login);
            window.location.reload();
        });
    },

    /**
     * Checks whether user is administrator.
     * @param user Optional parameter, if not specified, the currently logged in user is tested
     * @return {boolean}
     */
    isAdmin: function (user) {
        const userToTest = user ? user : UserStore.getCurrentUser();
        if (!userToTest) {
            return false;
        }
        return userToTest.types && userToTest.types.indexOf(Vocabulary.ADMIN_TYPE) !== -1;
    },

    /**
     * Checks whether the currently logged in user can view patient records of the specified clinics.
     *
     * To be able to view the records, the user has to be an admin or a member of the clinic.
     * @param clinicKey Key of the clinic to test
     * @return {*|boolean}
     */
    canLoadClinicsPatients(clinicKey) {
        const currentUser = UserStore.getCurrentUser();
        return currentUser !== null && (this.isAdmin(currentUser) || (currentUser.clinic !== null && currentUser.clinic.key === clinicKey));
    }
};

module.exports = Authentication;