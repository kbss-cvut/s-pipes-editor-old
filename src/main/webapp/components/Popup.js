'use strict';

var React = require('react');
import RecordController from "../components/record/RecordController";

var injectIntl = require('../utils/injectIntl');

var Popup = React.createClass({
    render: function () {
        return <div id="myModal" className="modal">
            <div className="modal-content">
                <div className="RecordContr"><RecordController/></div>
            </div>
        </div>
    },
});

module.exports = injectIntl(Popup);
