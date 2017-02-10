'use strict';

import React from 'react';

import HelpIcon from '../HelpIcon';
import I18nWrapper from '.././I18nWrapper';
import injectIntl from '../../utils/injectIntl';
import Input from '../Input';

class RequiredAttributes extends React.Component {
    static propTypes = {
        record: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        completed: React.PropTypes.bool.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = this.props.i18n;
    }

    render() {
        var record = this.props.record;
        // If the 'completed' prop is true, the attributes (except for the name) should be read only
        return <div>
            <div className='row'>
                <div className='col-xs-4'>
                    <Input type='text' name='localName' value={record.localName}
                           label={this.i18n('records.local-name') + '*'} onChange={this.props.onChange}
                           labelClassName='col-xs-4' wrapperClassName='col-xs-8'/>
                </div>
                <HelpIcon text={this.i18n('help.local-name')}/>
            </div>
        </div>
    }
}

export default injectIntl(I18nWrapper(RequiredAttributes));
