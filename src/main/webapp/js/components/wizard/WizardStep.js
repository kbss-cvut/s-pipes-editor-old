'use strict';

import React from 'react';
import {Alert, Button, ButtonToolbar, Panel} from 'react-bootstrap';
import {Constants, HelpIcon} from 'semforms';
import injectIntl from '../../utils/injectIntl';
import I18nMixin from '../../i18n/I18nMixin';
import WizardStore from '../../stores/WizardStore';
import {default as JsonLdUtils} from 'jsonld-utils';

let WizardStep = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        onClose: React.PropTypes.func,
        onFinish: React.PropTypes.func.isRequired,
        onAdvance: React.PropTypes.func,
        onRetreat: React.PropTypes.func,
        onNext: React.PropTypes.func,
        onPrevious: React.PropTypes.func,
        title: React.PropTypes.string,
        stepIndex: React.PropTypes.number.isRequired,
        isFirstStep: React.PropTypes.bool,
        isLastStep: React.PropTypes.bool,
        defaultNextDisabled: React.PropTypes.bool
    },

    getInitialState: function () {
        return {
            advanceDisabled: this.props.defaultNextDisabled != null ? this.props.defaultNextDisabled : false,
            retreatDisabled: false
        };
    },

    onAdvance: function (err) {
        if (err) {
            this.setState({
                advanceDisabled: false,
                retreatDisabled: false,
                currentError: err
            });
        } else {
            WizardStore.updateStepData(this.props.stepIndex, this.getStepData());
            this.props.onAdvance();
        }
    },

    getStepData: function () {
        return this.refs.component.getData ? this.refs.component.getData() : null;
    },

    onNext: function () {
        this.setState({
            advanceDisabled: true,
            retreatDisabled: true
        });
        if (this.props.onNext) {
            this.props.onNext.apply(this, [this.onAdvance]);
        } else {
            WizardStore.updateStepData(this.props.stepIndex, this.getStepData());
            this.props.onAdvance();
        }
    },

    onPrevious: function () {
        if (this.props.onPrevious) {
            this.props.onPrevious.apply(this, [this.props.onRetreat]);
        } else {
            this.props.onRetreat();
        }
    },

    onFinish: function () {
        WizardStore.updateStepData(this.props.stepIndex, this.getStepData());
        this.props.onFinish();
    },

    enableNext: function () {
        this.setState({advanceDisabled: false});
    },

    disableNext: function () {
        this.setState({advanceDisabled: true});
    },


    render: function () {
        let previousButton;
        if (!this.props.isFirstStep) {
            previousButton = (<Button onClick={this.onPrevious} disabled={this.state.retreatDisabled} bsStyle='primary'
                                      bsSize='small'>{this.i18n('wizard.previous')}</Button>);
        }
        let advanceButton = this.renderAdvanceButton();
        let error = null;
        if (this.state.currentError) {
            error = (<Alert bsStyle='danger'><p>{this.state.currentError.message}</p></Alert>);
        }
        let title = (<h4>{this.props.title}{this._renderHelpIcon()}</h4>);
        return (
            <div className='wizard-step'>
                <Panel header={title} bsStyle='primary' className='wizard-step-content'>
                    {this.renderComponent()}
                </Panel>
                <ButtonToolbar style={{float: 'right'}}>
                    {previousButton}
                    {advanceButton}
                </ButtonToolbar>
                {error}
            </div>
        );
    },

    _renderHelpIcon: function() {
        const question = WizardStore.getStepData([this.props.stepIndex]);
        return question[Constants.HELP_DESCRIPTION] ?
            <HelpIcon text={JsonLdUtils.getLocalized(question[Constants.HELP_DESCRIPTION], this.props.intl)}
                      iconClass='help-icon-section'/> : null;
    },

    renderAdvanceButton: function () {
        let disabledTitle = this.state.advanceDisabled ? this.i18n('wizard.advance-disabled-tooltip') : null;
        let button = null;
        if (!this.props.isLastStep) {
            button =
                <Button onClick={this.onNext} disabled={this.state.advanceDisabled} bsStyle='primary' bsSize='small'
                        title={disabledTitle}>{this.i18n('wizard.next')}</Button>;
        }
        return button;
    },

    renderComponent: function () {
        return React.createElement(this.props.component, {
            ref: 'component',
            stepIndex: this.props.stepIndex,
            enableNext: this.enableNext,
            disableNext: this.disableNext,
            next: this.onNext,
            previous: this.onPrevious,
            finish: this.onFinish,
            insertStepAfterCurrent: this.props.onInsertStepAfterCurrent,
            addStep: this.props.onAddStep,
            removeStep: this.props.onRemoveStep
        });
    }
});

module.exports = injectIntl(WizardStep);