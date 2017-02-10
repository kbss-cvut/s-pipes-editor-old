'use strict';

import React from 'react';
import {Button, Panel} from 'react-bootstrap';

import injectIntl from '../../utils/injectIntl';
import I18nWrapper from '.././I18nWrapper';
import Mask from '../Mask';
import ClinicTable from './ClinicTable';

class Clinics extends React.Component {
    static propTypes = {
        clinics: React.PropTypes.array,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
    }

    render() {
        var clinics = this.props.clinics;
        if (clinics === null) {
            return <Mask text={this.i18n('please-wait')}/>;
        }
        return <Panel header={this.i18n('clinics.panel-title')} bsStyle='primary'>
            <ClinicTable {...this.props}/>
            <div>
                <Button bsStyle='primary'
                        onClick={this.props.handlers.onCreate}>{this.i18n('clinics.create-clinic')}</Button>
            </div>
        </Panel>
    }
}

export default injectIntl(I18nWrapper(Clinics));
