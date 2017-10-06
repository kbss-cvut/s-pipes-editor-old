'use strict';

const Constants = require('../constants/Constants');

const CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

module.exports = {

    searchResult: undefined,

    /**
     * Finds value in a JSON tree based on key.
     * Returns undefined in case nothing is found.
     * @param tree JSON tree
     * @param uri URI to search for
     */
    find: function (tree, uri) {
        this.findObjectInTree(tree, uri);
        return this.searchResult;
    },

    findObjectInTree: function (tree, uri) {
        if (typeof(tree) === "object" && tree !== null) {
            if (uri === tree["uri"])
                this.searchResult = tree;
            else
                for (let p in tree)
                    if (tree.hasOwnProperty(p))
                        this.findObjectInTree(tree[p], uri);
        }
    },

    /**
     * Formats the specified date into DD-MM-YY HH:mm
     * @param date The date to format
     */
    formatDate: function (date) {
        const day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate().toString();
        const month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1).toString();
        const year = (date.getFullYear() % 100).toString();
        const h = date.getHours();
        const hour = h < 10 ? '0' + h : h.toString();
        const minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes().toString();
        return (day + '-' + month + '-' + year + ' ' + hour + ':' + minute);
    },

    /**
     * Extracts report key from location header in the specified Ajax response.
     * @param response Ajax response
     * @return {string} Report key as string
     */
    extractKeyFromLocationHeader: function (response) {
        const location = response.headers['location'];
        if (!location) {
            return '';
        }
        return location.substring(location.lastIndexOf('/') + 1);
    },

    /**
     * Extracts application path from the current window location.
     *
     * I.e. if the current hash is '#/reports?_k=312312', the result will be 'reports'
     * @return {String}
     */
    getPathFromLocation: function () {
        const hash = window.location.hash;
        const result = /#[/]?([a-z/0-9]+)\?/.exec(hash);
        return result ? result[1] : '';
    },

    generatePassword: function () {
        let pass = '';
        for (let i = 0; i < Constants.PASSWORD_LENGTH; i++) {
            pass += CHARACTERS.charAt(Math.floor(Math.random() * CHARACTERS.length));
        }
        return pass;
    }
};
