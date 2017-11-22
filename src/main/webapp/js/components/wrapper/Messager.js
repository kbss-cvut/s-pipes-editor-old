'use strict';

import React from 'react';
import {Alert} from 'react-bootstrap';

const dismissInterval = 7000;

const Messager = (Component) => class extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            displayMessage: false,
            message: null
        };
    }

    dismissMessage = () => {
        this.setState({message: null});
    };

    showInfoMessage = (text) => {
        this._showMessage('info', text);
    };

    _showMessage = (type, text) => {
        this.setState({
            message: {
                type: type,
                text: text
            }
        });
    };

    showSuccessMessage = (text) => {
        this._showMessage('success', text);
    };

    showErrorMessage = (text) => {
        this._showMessage('danger', text);
    };

    showWarnMessage = (text) => {
        this._showMessage('warning', text);
    };

    renderMessage() {
        return this.state.message ? <div className='form-group'>
            <Alert bsStyle={this.state.message.type} onDismiss={this.dismissMessage}
                   dismissAfter={dismissInterval}>
                <p>{this.state.message.text}</p>
            </Alert>
        </div> : null;
    }

    render() {
        return <div>
            {this.renderMessage()}
            <Component showInfoMessage={this.showInfoMessage} showSuccessMessage={this.showSuccessMessage}
                       showErrorMessage={this.showErrorMessage}
                       showWarnMessage={this.showWarnMessage} {...this.props}/>
        </div>;
    }
};

export default Messager;