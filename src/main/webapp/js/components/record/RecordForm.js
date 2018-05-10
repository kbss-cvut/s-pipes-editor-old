'use strict';

import React from 'react';
import I18nWrapper from '../../i18n/I18nWrapper';
import injectIntl from '../../utils/injectIntl';
import Mask from '../Mask';
import Wizard from '../wizard/Wizard';
import WizardBuilder from '../wizard/generator/WizardBuilder';
import WizardStore from '../../stores/WizardStore';

class RecordForm extends React.Component {
    static propTypes = {
        record: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            wizardProperties: null
        }
    }

    componentDidMount() {
        WizardBuilder.generateWizard(this.props.script, this.props.module, this.props.moduleType, this.props.record, this.onWizardReady);
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.record.question !== nextProps.record.question) {
            WizardBuilder.generateWizard(this.props.script, this.props.module, this.props.moduleType, nextProps.record, this.onWizardReady);
        }
    }

    onWizardReady = (wizardProperties) => {
        this.setState({wizardProperties: wizardProperties});
    };

    getFormData = () => {
        return WizardStore.getData()["root"];
    };

    render() {
        if (!this.state.wizardProperties) {
            return <Mask text={this.i18n('record.form.please-wait')}/>;
        }
        return <Wizard steps={this.state.wizardProperties.steps} enableForwardSkip={true}/>
    }
}

export default injectIntl(I18nWrapper(RecordForm), {withRef: true});