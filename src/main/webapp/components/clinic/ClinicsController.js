'use strict';

import React from "react";
import Actions from "../../actions/Actions";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import Clinics from "./Clinics";
import ClinicStore from "../../stores/ClinicStore";

export default class ClinicsController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            clinics: ClinicStore.getClinics()
        };
    }

    componentDidMount() {
        Actions.loadAllClinics();
        this.unsubscribe = ClinicStore.listen(this._onClinicsLoaded);
    }

    _onClinicsLoaded = (data) => {
        if (data.action !== Actions.loadAllClinics) {
            return;
        }
        this.setState({clinics: data.data});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onEditClinic = (clinic) => {
        Routing.transitionTo(Routes.editClinic, {
            params: {key: clinic.key},
            handlers: {
                onCancel: Routes.clinics
            }
        });
    };

    _onAddClinic = () => {
        Routing.transitionTo(Routes.createClinic, {
            handlers: {
                onSuccess: Routes.clinics,
                onCancel: Routes.clinics
            }
        });
    };

    _onDeleteClinic = (clinic) => {
        Actions.deleteClinic(clinic, Actions.loadAllClinics);
    };

    render() {
        var handlers = {
            onEdit: this._onEditClinic,
            onCreate: this._onAddClinic,
            onDelete: this._onDeleteClinic
        };
        return <Clinics clinics={this.state.clinics} handlers={handlers}/>;
    }
}
