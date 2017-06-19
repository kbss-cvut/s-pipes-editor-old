'use strict';

import React from 'react';
import assign from 'object-assign';

import Actions from '../../actions/Actions';
import EntityFactory from '../../utils/EntityFactory';
import injectIntl from '../../utils/injectIntl';
import I18nWrapper from '../../i18n/I18nWrapper';
import Messager from '../wrapper/Messager';
import Record from './Record';
import RecordStore from '../../stores/RecordStore';
import RouterStore from '../../stores/RouterStore';
import Routes from '../../utils/Routes';
import Routing from '../../utils/Routing';

class RecordController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            record: this._isNew() ? EntityFactory.initNewPatientRecord() : null,
            loading: false
        };
    }

    _isNew() {
        return !this.props.params.key;
    }

    componentDidMount() {
        if (!this.state.record) {
            Actions.loadRecord(this.props.params.key);
            this.setState({loading: true});
        }
        this.unsubscribe = RecordStore.listen(this._onRecordLoaded);
    }

    _onRecordLoaded = (data) => {
        if (data.action === Actions.loadRecord) {
            this.setState({record: data.data, loading: false});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onSave = () => {
        var record = this.state.record;
        record.question = this.recordComponent.refs.wrappedInstance.getWrappedComponent().getFormData();
        if (record.isNew) {
            delete record.isNew;
            Actions.createRecord(record, this._onSaveSuccess, this._onSaveError);
        } else {
            Actions.updateRecord(record, this._onSaveSuccess, this._onSaveError);
        }
    };

    _onSaveSuccess = (newKey) => {
        this.props.showSuccessMessage(this.props.i18n('record.save-success'));
        Actions.loadRecord(newKey ? newKey : this.props.params.key);
    };

    _onSaveError = (err) => {
        this.props.showErrorMessage(this.props.formatMessage('record.save-error', {error: err.message}));
    };

    _onCancel = () => {
        var handlers = RouterStore.getViewHandlers(Routes.editRecord.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Routes.dashboard);
        }
    };

    _onChange = (change) => {
        var update = assign({}, this.state.record, change);
        this.setState({record: update});
    };

    render() {
        var handlers = {
            onSave: this._onSave,
            onCancel: this._onCancel,
            onChange: this._onChange
        };
        return <Record ref={(c) => this.recordComponent = c} handlers={handlers} record={this.state.record}
                       loading={this.state.loading}/>;
    }
}

export default injectIntl(I18nWrapper(Messager(RecordController)));
