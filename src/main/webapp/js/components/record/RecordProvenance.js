'use strict';

import React from 'react';
import {FormattedMessage} from 'react-intl';

import Utils from '../../utils/Utils';

const RecordProvenance = (props) => {
    let record = props.record;
    if (record.isNew) {
        return null;
    }
    let author = record.author ? record.author.firstName + ' ' + record.author.lastName : '',
        created = Utils.formatDate(new Date(record.dateCreated)),
        lastEditor, lastModified;
    if (!record.lastModified) {
        return <div className='notice-small'>
            <FormattedMessage id='record.created-by-msg'
                              values={{date: created, name: <b>{author}</b>}}/>
        </div>;
    }
    lastEditor = record.lastModifiedBy ? record.lastModifiedBy.firstName + ' ' + record.lastModifiedBy.lastName : '';
    lastModified = Utils.formatDate(new Date(record.lastModified));
    return <div className='notice-small'>
        <FormattedMessage id='record.created-by-msg'
                          values={{date: created, name: <b>{author}</b>}}/>
        &nbsp;
        <FormattedMessage id='record.last-edited-msg'
                          values={{date: lastModified, name: <b>{lastEditor}</b>}}/>
    </div>;
};

RecordProvenance.propTypes = {
    record: React.PropTypes.object
};

export default RecordProvenance;
