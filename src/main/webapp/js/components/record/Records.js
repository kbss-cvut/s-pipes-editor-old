'use strict';

import React from "react";
import {Button, Panel} from "react-bootstrap";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Mask from "../Mask";
import RecordTable from "./RecordTable";

class Records extends React.Component {
    static propTypes = {
        records: React.PropTypes.array,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
    }

    render() {
        let records = this.props.records;
        if (records === null) {
            return <Mask text={this.i18n('please-wait')}/>;
        }
        return <Panel header={this.i18n('records.panel-title')} bsStyle='primary'>
            <RecordTable {...this.props}/>
            <div>
                <Button bsStyle='primary'
                        onClick={this.props.handlers.onCreate}>{this.i18n('dashboard.create-tile')}</Button>
            </div>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(Records));
