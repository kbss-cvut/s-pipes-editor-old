/**
 * Created by yan on 19.06.17.
 */

'use strict';

import React from "react";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Messager from "../wrapper/Messager";

class ViewController extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <h1>Hello world!</h1>
        );
    }
}

export default injectIntl(I18nWrapper(Messager(ViewController)));