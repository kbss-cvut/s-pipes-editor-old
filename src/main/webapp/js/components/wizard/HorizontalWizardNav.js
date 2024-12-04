'use strict';

import React from 'react';
import {Nav, NavItem} from 'react-bootstrap';
import {FormUtils} from 's-forms';

const HorizontalWizardNav = (props) => {
    let navMenu = props.steps.map((step, index) => {
        return <NavItem key={'nav' + index} eventKey={index}
                        id={'wizard-nav-' + index}
                        active={index === props.currentStep}
                        disabled={! FormUtils.isRelevant(step.data)}>{step.name}</NavItem>;
    }); //TODO add "disabled" to VerticalWizardNav

    return <div className="wizard-nav">
        <Nav bsStyle="tabs" onSelect={(key) => props.onNavigate(key)}>
            {navMenu}
        </Nav>
    </div>;
};

HorizontalWizardNav.propTypes = {
    currentStep: React.PropTypes.number.isRequired,
    steps: React.PropTypes.array.isRequired,
    onNavigate: React.PropTypes.func.isRequired
};

export default HorizontalWizardNav;