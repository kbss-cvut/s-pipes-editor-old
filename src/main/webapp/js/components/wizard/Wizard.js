'use strict';

import React from 'react';
import Reflux from 'reflux';
import classNames from 'classnames';

import WizardStep from './WizardStep';
import WizardStore from '../../stores/WizardStore';

import {default as HorizontalWizardNav} from './HorizontalWizardNav';
import {default as VerticalWizardNav} from './VerticalWizardNav';

const IS_HORIZONTAL = true;

let Wizard = React.createClass({
    mixins: [Reflux.listenTo(WizardStore, '_onWizardDataChange')],

    propTypes: {
        start: React.PropTypes.number,
        steps: React.PropTypes.array,
        onFinish: React.PropTypes.func,
        onClose: React.PropTypes.func,
        enableForwardSkip: React.PropTypes.bool     // Whether to allow forward step skipping
    },

    getInitialState: function () {
        // First step is visited as soon as the wizard opens
        if (this.props.steps.length > 0) {
            this.props.steps[0].visited = true;
        }
        return {
            currentStep: this.props.start || 0,
            nextDisabled: false,
            previousDisabled: false
        };
    },

    getDefaultProps: function () {
        return {
            steps: []
        };
    },

    _onWizardDataChange: function() {
        this.forceUpdate();
    },

    onAdvance: function () {
        let change = {};
        if (this.state.currentStep !== this.props.steps.length - 1) {
            this.props.steps[this.state.currentStep + 1].visited = true;
            change.currentStep = this.state.currentStep + 1;
        }
        this.setState(change);
    },

    onRetreat: function () {
        if (this.state.currentStep === 0) {
            return;
        }
        this.setState({
            currentStep: this.state.currentStep - 1
        });
    },

    onFinish: function (errCallback) {
        let data = {
            data: WizardStore.getData(),
            stepData: WizardStore.getStepData()
        };
        WizardStore.reset();
        this.props.onFinish(data, this.props.onClose, errCallback);
    },

    /**
     * Insert the specified step after the current one.
     * @param step The step to insert
     */
    onInsertStepAfterCurrent: function (step) {
        this.props.steps.splice(this.state.currentStep + 1, 0, step);
        WizardStore.insertStep(this.state.currentStep + 1, step.data);
    },

    /**
     * Adds the specified step to the end of this wizard.
     * @param step The step to add
     */
    onAddStep: function (step) {
        this.props.steps.push(step);
        WizardStore.insertStep(this.props.steps.length - 1, step.data);
    },

    onRemoveStep: function (stepId) {
        let stateUpdate = {};
        for (let i = 0, len = this.props.steps.length; i < len; i++) {
            if (this.props.steps[i].id === stepId) {
                this.props.steps.splice(i, 1);
                WizardStore.removeStep(i);
                if (i === this.state.currentStep && i !== 0) {
                    stateUpdate.currentStep = this.state.currentStep - 1;
                }
                break;
            }
        }
        this.setState(stateUpdate);
    },


    render: function () {
        let component = this.initComponent();

        let navMenu,
            componentClass = classNames('wizard-content', {'col-xs-10': !IS_HORIZONTAL});

        if (IS_HORIZONTAL) {
            navMenu = <HorizontalWizardNav currentStep={this.state.currentStep} steps={this.props.steps}
                                           onNavigate={this.navigate}/>;
        } else {
            navMenu = <VerticalWizardNav currentStep={this.state.currentStep} steps={this.props.steps}
                                         onNavigate={this.navigate}/>
        }

        return <div className="wizard">
            {navMenu}
            <div className={componentClass}>
                {component}
            </div>
        </div>;
    },

    navigate: function (stepIndex) {

        if (stepIndex === this.state.currentStep || stepIndex >= this.props.steps.length) {
            return;
        }
        // Can we jump forward?
        if (stepIndex > this.state.currentStep && !this.props.steps[stepIndex].visited && !this.props.enableForwardSkip) {
            return;
        }
        this.setState({
            currentStep: stepIndex
        });
    },

    initComponent: function () {
        if (this.props.steps.length === 0) {
            return <div className='italics'>There are no steps in this wizard.</div>;
        }
        let step = this.props.steps[this.state.currentStep];

        return React.createElement(WizardStep, {
            key: 'step' + this.state.currentStep,
            onClose: this.props.onClose,
            onFinish: this.onFinish,
            onAdvance: this.onAdvance,
            onRetreat: this.onRetreat,
            onNext: step.onNext,
            onPrevious: step.onPrevious,
            onInsertStepAfterCurrent: this.onInsertStepAfterCurrent,
            onAddStep: this.onAddStep,
            onRemoveStep: this.onRemoveStep,
            component: step.component,
            title: step.name,
            stepIndex: this.state.currentStep,
            isFirstStep: this.state.currentStep === 0,
            isLastStep: this.state.currentStep === this.props.steps.length - 1,
            defaultNextDisabled: step.defaultNextDisabled
        });
    }
});

module.exports = Wizard;
