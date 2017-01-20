'use strict';

import React from "react";
import {Button, Table} from "react-bootstrap";

import Authentication from "../../utils/Authentication";
import DeleteItemDialog from "../DeleteItemDialog";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Routes from "../../utils/Routes";

class ClinicTable extends React.Component {
    static propTypes = {
        clinics: React.PropTypes.array.isRequired,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            showDialog: false,
            selectedItem: null
        }
    }

    _onDelete = (item) => {
        this.setState({showDialog: true, selectedItem: item});
    };

    _onCancelDelete = () => {
        this.setState({showDialog: false, selectedItem: null});
    };

    _onSubmitDelete = () => {
        this.props.handlers.onDelete(this.state.selectedItem);
        this.setState({showDialog: false, selectedItem: null});
    };

    render() {
        return <div>
            <DeleteItemDialog onClose={this._onCancelDelete} onSubmit={this._onSubmitDelete}
                              show={this.state.showDialog} item={this.state.selectedItem}
                              itemLabel={this._getDeleteLabel()}/>
            <Table striped bordered condensed hover>
                {this._renderHeader()}
                <tbody>
                {this._renderRows()}
                </tbody>
            </Table>
        </div>;
    }

    _getDeleteLabel() {
        var clinic = this.state.selectedItem;
        return clinic ? clinic.name : '';
    }

    _renderHeader() {
        return <thead>
        <tr>
            <th className='col-xs-5 content-center'>{this.i18n('name')}</th>
            <th className='col-xs-5 content-center'>{this.i18n('clinic.email')}</th>
            <th className='col-xs-2 content-center'>{this.i18n('actions')}</th>
        </tr>
        </thead>;
    }

    _renderRows() {
        var items = this.props.clinics,
            rows = [],
            onEdit = this.props.handlers.onEdit;
        for (var i = 0, len = items.length; i < len; i++) {
            rows.push(<ClinicRow key={items[i].name} clinic={items[i]} onEdit={onEdit} onDelete={this._onDelete}/>);
        }
        return rows;
    }
}

var ClinicRow = (props) => {
    var clinic = props.clinic,
        deleteButton = Authentication.isAdmin() ?
            <Button bsStyle='warning' bsSize='small' title={props.i18n('clinics.delete-tooltip')}
                    onClick={() => props.onDelete(props.clinic)}>{props.i18n('delete')}</Button> : null;

    return <tr>
        <td className='report-row'>
            <a href={'#/' + Routes.clinics.path + '/' + clinic.key}
               title={props.i18n('clinics.open-tooltip')}>{clinic.name}</a>
        </td>
        <td className='report-row'>{clinic.emailAddress}</td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small' title={props.i18n('clinics.open-tooltip')}
                    onClick={() => props.onEdit(props.clinic)}>{props.i18n('open')}</Button>
            {deleteButton}
        </td>
    </tr>;
};

ClinicRow.propTypes = {
    clinic: React.PropTypes.object.isRequired,
    onEdit: React.PropTypes.func.isRequired,
    onDelete: React.PropTypes.func.isRequired
};

ClinicRow = injectIntl(I18nWrapper(ClinicRow));

export default injectIntl(I18nWrapper(ClinicTable));
