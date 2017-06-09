'use strict';

import React from "react";
import {Button, Table} from "react-bootstrap";
import DeleteItemDialog from "../DeleteItemDialog";
import HelpIcon from "../HelpIcon";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import RecordValidator from "../../validation/RecordValidator";
import Routes from "../../utils/Routes";
import Utils from "../../utils/Utils";

class RecordTable extends React.Component {
    static propTypes = {
        records: React.PropTypes.array.isRequired,
        handlers: React.PropTypes.object.isRequired,
        disableDelete: React.PropTypes.bool
    };

    static defaultProps = {
        disableDelete: false
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
        this.state = {
            selectedRecord: null,
            showDialog: false
        };
    }

    _onDelete = (record) => {
        this.setState({showDialog: true, selectedRecord: record});
    };

    _onCancelDelete = () => {
        this.setState({showDialog: false, selectedRecord: null});
    };

    _onSubmitDelete = () => {
        this.props.handlers.onDelete(this.state.selectedRecord);
        this.setState({showDialog: false, selectedRecord: null});
    };

    render() {
        return <div>
            <DeleteItemDialog onClose={this._onCancelDelete} onSubmit={this._onSubmitDelete}
                              show={this.state.showDialog} item={this.state.selectedRecord}
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
        return this.state.selectedRecord ? this.state.selectedRecord.localName : '';
    }

    _renderHeader() {
        return <thead>
        <tr>
            <th className='col-xs-6 content-center'>{this.i18n('records.local-name')}</th>
            <th className='col-xs-3 content-center'>{this.i18n('records.last-modified')}</th>
            <th className='col-xs-1 content-center'>{this.i18n('records.completion-status')}</th>
            <th className='col-xs-2 content-center'>{this.i18n('actions')}</th>
        </tr>
        </thead>
    }

    _renderRows() {
        var records = this.props.records,
            rows = [],
            onEdit = this.props.handlers.onEdit;
        for (var i = 0, len = records.length; i < len; i++) {
            rows.push(<RecordRow key={records[i].key} record={records[i]} onEdit={onEdit} onDelete={this._onDelete}
                                 disableDelete={this.props.disableDelete}/>);
        }
        return rows;
    }
}

var RecordRow = (props) => {
    var record = props.record,
        isComplete = RecordValidator.isComplete(record),
        completionTooltip = props.i18n(isComplete ? 'records.completion-status-tooltip.complete' : 'records.completion-status-tooltip.incomplete'),
        deleteButton = props.disableDelete ? null :
            <Button bsStyle='warning' bsSize='small' title={props.i18n('records.delete-tooltip')}
                    onClick={() => props.onDelete(record)}>{props.i18n('delete')}</Button>;
    return <tr>
        <td className='report-row'><a href={'#/' + Routes.records.path + '/' + record.key}>{record.localName}</a></td>
        <td className='report-row content-center'>
            {Utils.formatDate(new Date(record.lastModified ? record.lastModified : record.dateCreated))}
        </td>
        <td className='report-row content-center'>
            <HelpIcon text={completionTooltip} glyph={isComplete ? 'ok' : 'remove'}/>
        </td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small' title={props.i18n('records.open-tooltip')}
                    onClick={() => props.onEdit(record)}>{props.i18n('open')}</Button>
            {deleteButton}
        </td>
    </tr>
};

RecordRow.propTypes = {
    record: React.PropTypes.object.isRequired,
    onEdit: React.PropTypes.func.isRequired,
    onDelete: React.PropTypes.func.isRequired,
    disableDelete: React.PropTypes.bool.isRequired
};

RecordRow = injectIntl(I18nWrapper(RecordRow));

export default injectIntl(I18nWrapper(RecordTable));
