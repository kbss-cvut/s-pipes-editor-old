'use strict';

import React from 'react';
import assign from 'object-assign';
import Wizard from './Wizard';
import {Modal as Modal} from 'react-bootstrap';

const WizardWindow = React.createClass({
    propTypes: {
        onHide: React.PropTypes.func,
        title: React.PropTypes.string,
        show: React.PropTypes.bool
    },

    render: function () {
        let properties = assign({}, this.props, {onClose: this.props.onHide});
        return (
            <Modal {...this.props} show={this.props.show} bsSize="large" title={this.props.title} animation={true}
                                   dialogClassName="large-modal">
                <Modal.Header closeButton>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>

                <div className="modal-body" style={{overflow: 'hidden'}}>
                    <Wizard {...properties}/>
                </div>
            </Modal>
        );
    }
});

module.exports = WizardWindow;