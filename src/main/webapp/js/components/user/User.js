'use strict';

import React from "react";
import {Button, Panel} from "react-bootstrap";
import Actions from "../../actions/Actions";
import Authentication from "../../utils/Authentication";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Input from "../Input";
import Mask from "../Mask";
import Select from "../Select";
import UserValidator from "../../validation/UserValidator";
import Vocabulary from "../../constants/Vocabulary";

class User extends React.Component {
    static propTypes = {
        user: React.PropTypes.object,
        loading: React.PropTypes.bool,
        onSave: React.PropTypes.func.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            clinics: ClinicStore.getClinics() ? User._processClinics(ClinicStore.getClinics()) : []
        };
    }

    componentDidMount() {
        if (this.state.clinics.length === 0) {
            Actions.loadAllClinics();
        }
        this.unsubscribe = ClinicStore.listen(this._onClinicsLoaded);
    }

    _onClinicsLoaded = (data) => {
        if (data.action === Actions.loadAllClinics) {
            this.setState({
                clinics: User._processClinics(data.data)
            });
        }
    };

    static _processClinics(clinics) {
        return clinics.map((item) => {
            return {label: item.name, value: item.uri}
        });
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onChange = (e) => {
        var change = {};
        change[e.target.name] = e.target.value;
        this.props.onChange(change);
    };

    _onClinicSelected = (e) => {
        var value = e.target.value,
            clinic = ClinicStore.getClinics().find((item) => item.uri === value),
            change = {
                clinic: clinic
            };
        this.props.onChange(change);
    };

    _onAdminStatusChange = (e) => {
        var isAdmin = e.target.checked,
            types = this.props.user.types.slice();
        if (isAdmin) {
            types.push(Vocabulary.ADMIN_TYPE);
        } else {
            types.splice(types.indexOf(Vocabulary.ADMIN_TYPE), 1);
        }
        this.props.onChange({types: types});
    };

    render() {
        if (this.props.loading) {
            return <Mask text={this.i18n('please-wait')}/>;
        }
        var user = this.props.user;
        return <Panel header={<h3>{this.i18n('user.panel-title')}</h3>} bsStyle='primary'>
            <form className='form-horizontal' style={{margin: '0.5em 0 0 0'}}>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='firstName' label={this.i18n('user.first-name')}
                               value={user.firstName}
                               labelClassName='col-xs-4' wrapperClassName='col-xs-8' onChange={this._onChange}/>
                    </div>
                    <div className='col-xs-4'>
                        <Input type='text' name='lastName' label={this.i18n('user.last-name')}
                               value={user.lastName}
                               labelClassName='col-xs-4' wrapperClassName='col-xs-8' onChange={this._onChange}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='username' label={this.i18n('user.username')}
                               value={user.username}
                               labelClassName='col-xs-4' wrapperClassName='col-xs-8' onChange={this._onChange}/>
                    </div>
                    <div className='col-xs-4'>
                        <Input type='text' name='emailAddress' label={this.i18n('users.email')}
                               value={user.emailAddress}
                               labelClassName='col-xs-4' wrapperClassName='col-xs-8' onChange={this._onChange}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='password' label={this.i18n('user.password')}
                               labelClassName='col-xs-4' readOnly={true} value={user.password}
                               wrapperClassName='col-xs-8'/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Select options={this.state.clinics} name='clinic' label={this.i18n('clinic.panel-title')}
                                onChange={this._onClinicSelected} labelClassName='col-xs-4'
                                wrapperClassName='col-xs-8' addDefault={true}
                                value={user.clinic ? user.clinic.uri : ''}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <div className='col-xs-4'>&nbsp;</div>
                        <div className='col-xs-8' style={{padding: '0 0 0 25px'}}>
                            <Input type='checkbox' checked={Authentication.isAdmin(user)}
                                   onChange={this._onAdminStatusChange}
                                   label={this.i18n('user.is-admin')} inline={true}
                                   disabled={!Authentication.isAdmin()}/>
                        </div>
                    </div>
                </div>
                <div style={{margin: '1em 0em 0em 0em', textAlign: 'center'}}>
                    <Button bsStyle='success' bsSize='small' ref='submit'
                            disabled={!UserValidator.isValid(user) || this.props.loading}
                            onClick={this.props.onSave}>{this.i18n('save')}</Button>
                    <Button bsStyle='link' bsSize='small' onClick={this.props.onCancel}>{this.i18n('cancel')}</Button>
                </div>
            </form>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(User));
