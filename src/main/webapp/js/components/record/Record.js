'use strict';

import React from 'react';
import {Button, Panel} from 'react-bootstrap';
import {FormattedMessage} from 'react-intl';
import I18nWrapper from '../../i18n/I18nWrapper';
import injectIntl from '../../utils/injectIntl';
import Mask from '../Mask';
import RecordForm from './RecordForm';

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
        return <Panel header={this._renderHeader()} bsStyle='primary'>
            {this._renderForm()}
            {this._renderButtons()}
        </Panel>;
    }

    _renderHeader() {
        return <h3>
            <FormattedMessage id='record.panel-title'/>
        </h3>;
    }

    _renderForm() {
        return <RecordForm
            script={this.props.script}
            moduleType={this.props.moduleType}
            module={this.props.module}
            functionUri={this.props.functionUri}
            ref={(c) => this.form = c}
            record={this.props.record}/>;
    }

    _renderButtons() {
        return <div style={{margin: '1em 0em 0em 0em', textAlign: 'center'}}>
            <Button bsStyle='success' bsSize='small' disabled={this.props.loading}
                    onClick={this.props.handlers.onSave}>{this.i18n('save')}</Button>
            <Button bsStyle='link' bsSize='small' onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
        </div>
    }
}

export default injectIntl(I18nWrapper(Record), {withRef: true});