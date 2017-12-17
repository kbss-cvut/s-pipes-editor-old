'use strict';

import React from 'react';
import {Button, Panel} from 'react-bootstrap';
import {FormattedMessage} from 'react-intl';
import I18nWrapper from '../../i18n/I18nWrapper';
import injectIntl from '../../utils/injectIntl';
import Input from '../Input';
import Mask from '../Mask';
import RecordForm from './RecordForm';
import RecordProvenance from './RecordProvenance';
import WizardStore from '../../stores/WizardStore';
import {FormUtils} from 'semforms';

class Record extends React.Component {
    static propTypes = {
        record: React.PropTypes.object,
        loading: React.PropTypes.bool,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
    }

    _onChange = (e) => {
        let change = {};
        change[e.target.name] = e.target.value;
        this.props.handlers.onChange(change);
    };

    getFormData = () => {
        return this.form.refs.wrappedInstance.getWrappedComponent().getFormData();
    };

    render() {
        if (this.props.loading || !this.props.record) {
            return <Mask text={this.i18n('please-wait')}/>;
        }
        let record = this.props.record,
            complete = true;
        return <Panel header={this._renderHeader()} bsStyle='primary'>
            <form className='form-horizontal'>
                {this._renderClinic()}
                <RecordProvenance record={record}/>
            </form>
            {this._renderForm(complete)}
            {this._renderButtons()}
        </Panel>;
    }

    _renderHeader() {
        let name = this.props.record.localName ? this.props.record.localName : '';
        return <h3>
            <FormattedMessage id='record.panel-title' values={{identifier: name}}/>
        </h3>;
    }

    _renderClinic() {
        let record = this.props.record;
        if (!record.clinic) {
            return null;
        }
        return <div className='row'>
            <div className='col-xs-4'>
                <Input type='text' value={record.clinic.name} label={this.i18n('record.clinic')}
                       labelClassName='col-xs-4' wrapperClassName='col-xs-8' readOnly/>
            </div>
        </div>;
    }

    _renderForm(completed) {
        return completed ? <RecordForm
            script={this.props.script}
            moduleType={this.props.moduleType}
            module={this.props.module}
            ref={(c) => this.form = c}
            record={this.props.record}/> : null;
    }

    _renderButtons() {
        return <div style={{margin: '1em 0em 0em 0em', textAlign: 'center'}}>
            <Button bsStyle='success' bsSize='small' disabled={this.props.loading || this._isFormInvalid()}
                    onClick={this.props.handlers.onSave}>{this.i18n('save')}</Button>
            <Button bsStyle='link' bsSize='small' onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
        </div>
    }

    _isFormInvalid() {
        return WizardStore.data ? FormUtils.isValid(WizardStore.data) : false;
    }
}

export default injectIntl(I18nWrapper(Record));