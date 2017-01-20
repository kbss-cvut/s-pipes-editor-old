'use strict';

import React from "react";
import assign from "object-assign";
import Actions from "../../actions/Actions";
import Authentication from "../../utils/Authentication";
import Clinic from "./Clinic";
import ClinicStore from "../../stores/ClinicStore";
import EntityFactory from "../../utils/EntityFactory";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Messager from "../wrapper/Messager";
import RecordStore from "../../stores/RecordStore";
import RouterStore from "../../stores/RouterStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import UserStore from "../../stores/UserStore";

class ClinicController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            clinic: this._isNew() ? EntityFactory.initNewClinic() : null,
            members: [],
            patients: [],
            loading: false
        };
    }

    _isNew() {
        return !this.props.params.key;
    }

    componentWillMount() {
        var clinicKey = this.props.params.key;
        if (!this.state.clinic) {
            Actions.loadClinic(clinicKey);
            this.setState({loading: true});
        }
        if (clinicKey) {
            Actions.loadClinicMembers(clinicKey);
            if (Authentication.canLoadClinicsPatients(clinicKey)) {
                Actions.loadClinicPatients(clinicKey);
            }
        }
        this.unsubscribe = ClinicStore.listen(this._onClinicLoaded);
        this.unsubscribeMembers = UserStore.listen(this._onMembersLoaded);
        this.unsubscribePatients = RecordStore.listen(this._onPatientsLoaded);
    }

    _onClinicLoaded = (data) => {
        if (data.action === Actions.loadClinic) {
            this.setState({clinic: data.data, loading: false});
        }
    };

    _onMembersLoaded = (data) => {
        if (data.action === Actions.loadClinicMembers && this.props.params.key === data.clinicKey) {
            this.setState({members: data.data});
        }
    };

    _onPatientsLoaded = (data) => {
        if (data.action === Actions.loadClinicPatients && this.props.params.key === data.clinicKey) {
            this.setState({patients: data.data});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
        this.unsubscribeMembers();
        this.unsubscribePatients();
    }

    _onSave = () => {
        var clinic = this.state.clinic;
        if (clinic.isNew) {
            delete clinic.isNew;
            Actions.createClinic(clinic, this._onSaveSuccess, this._onSaveError);
        } else {
            Actions.updateClinic(clinic, this._onSaveSuccess, this._onSaveError);
        }
    };

    _onSaveSuccess = (newKey) => {
        this.props.showSuccessMessage(this.props.i18n('clinic.save-success'));
        Actions.loadClinic(newKey ? newKey : this.props.params.key);
    };

    _onSaveError = (err) => {
        this.props.showErrorMessage(this.props.formatMessage('clinic.save-error', {error: err.message}));
    };

    _onCancel = () => {
        var handlers = RouterStore.getViewHandlers(Routes.editClinic.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Routes.clinics);
        }
    };

    _onChange = (change) => {
        var update = assign({}, this.state.clinic, change);
        this.setState({clinic: update});
    };

    render() {
        return <Clinic onSave={this._onSave} onCancel={this._onCancel} onChange={this._onChange}
                       clinic={this.state.clinic} members={this.state.members} patients={this.state.patients}
                       loading={this.state.loading}/>;
    }
}

export default injectIntl(I18nWrapper(Messager(ClinicController)));
