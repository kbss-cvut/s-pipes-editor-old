'use strict';

import {injectIntl as injectIntl} from 'react-intl';

/**
 * Our version of react-intl's injectIntl.
 *
 * Decorates the basic instance returned by injectIntl with accessors to the wrapped component or element (needed by
 * tests).
 */
module.exports = function (component, props) {
    if (!props) {
        props = {};
    }

    // Store this only for development purposes and specific components
    if (process.env.NODE_ENV !== 'production' || props.withRef) {
        const comp = injectIntl(component, props);
        props.withRef = true;
        comp.wrappedComponent = comp;
        return comp;
    } else {
        return injectIntl(component, props);
    }
};