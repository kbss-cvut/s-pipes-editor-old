'use strict';

import React from 'react';

import Actions from "../../actions/Actions";
import Records from "./Records";
import RecordStore from "../../stores/RecordStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";

export default class RecordsController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            records: RecordStore.getAllRecords(),
            loading: false
        }
    }

    componentDidMount() {
        if (!this.state.records) {
            Actions.loadAllRecords();
            this.setState({loading: true});
        }
        this.unsubscribe = RecordStore.listen(this._recordsLoaded);
    }

    _recordsLoaded = (data) => {
        if (data.action === Actions.loadAllRecords) {
            this.setState({records: data.data, loading: false});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onEditRecord = (record) => {
        Routing.transitionTo(Routes.editRecord, {
            params: {key: record.key},
            handlers: {
                onCancel: Routes.records
            }
        });
    };

    _onAddRecord = () => {
        Routing.transitionTo(Routes.createRecord, {
            handlers: {
                onSuccess: Routes.records,
                onCancel: Routes.records
            }
        });
    };

    _onDeleteRecord = (record) => {
        Actions.deleteRecord(record, Actions.loadAllRecords);
    };

    render() {
        let handlers = {
            onEdit: this._onEditRecord,
            onCreate: this._onAddRecord,
            onDelete: this._onDeleteRecord
        };
        return <Records records={this.state.records} handlers={handlers}/>;
    }
}
