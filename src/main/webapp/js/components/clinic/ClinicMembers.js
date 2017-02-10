'use strict';

import React from 'react';
import {Panel, Table} from 'react-bootstrap';

import injectInl from '../../utils/injectIntl';
import I18nWrapper from '.././I18nWrapper';

const ClinicMembers = (props) => {
    var members = props.members;
    if (members.length === 0) {
        return null;
    }

    var rows = [],
        member;
    for (var i = 0, len = members.length; i < len; i++) {
        member = members[i];
        rows.push(<tr key={member.username}>
            <td className='report-row'>{member.firstName + ' ' + member.lastName}</td>
            <td className='report-row'>{member.username}</td>
            <td className='report-row'>{member.emailAddress}</td>
        </tr>);
    }

    return <Panel header={<h3>{props.i18n('clinic.members.panel-title')}</h3>} bsStyle='info'>
        <Table striped bordered condensed hover>
            <thead>
            <tr>
                <th className='col-xs-4 content-center'>{props.i18n('name')}</th>
                <th className='col-xs-4 content-center'>{props.i18n('login.username')}</th>
                <th className='col-xs-4 content-center'>{props.i18n('users.email')}</th>
            </tr>
            </thead>
            <tbody>
            {rows}
            </tbody>
        </Table>
    </Panel>;
};

ClinicMembers.propTypes = {
    members: React.PropTypes.array.isRequired
};

export default injectInl(I18nWrapper(ClinicMembers));
