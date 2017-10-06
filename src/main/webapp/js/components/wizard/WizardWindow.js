'use strict';

let React = require('react');
let Modal = require('react-bootstrap').Modal;
let assign = require('object-assign');

let Wizard = require('./Wizard');

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
