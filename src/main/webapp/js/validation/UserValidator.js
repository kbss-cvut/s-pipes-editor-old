'use strict';

export default class UserValidator {

    static isValid(user) {
        if (!user.firstName || !user.lastName || !user.username) {
            return false;
        }
        return true;
    }
}