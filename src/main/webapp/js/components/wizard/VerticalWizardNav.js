'use strict';

import React from "react";
import {ListGroup, ListGroupItem} from "react-bootstrap";

const VerticalWizardNav = (props) => {

    let navMenu = props.steps.map((step, index) => {
        return <ListGroupItem key={'nav' + index} onClick={() => props.onNavigate(index)} id={'wizard-nav-' + index}
                              active={index === props.currentStep ? 'active' : ''}>{step.name}</ListGroupItem>;
    });

    return  <div className="wizard-nav col-xs-2">
        <ListGroup>
            {navMenu}
        </ListGroup>
    </div>;
};

VerticalWizardNav.propTypes = {
    currentStep: React.PropTypes.number.isRequired,
    steps: React.PropTypes.array.isRequired,
    onNavigate: React.PropTypes.func.isRequired
};


export default VerticalWizardNav;
