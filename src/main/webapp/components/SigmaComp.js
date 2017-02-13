'use strict';

var React = require('react');

var injectIntl = require('../utils/injectIntl');


var Sigma = React.createClass({
    render: function () {
        return <div>
            <div id="container"></div>
            <script type="text/javascript" src="modal.js"></script>
            <script type="text/javascript" src="graph.js"></script>
            <script type="text/javascript" src="nodeRenderer.js"></script>
            <script type="text/javascript" src="edgeRenderer.js"></script>
            <script type="text/javascript" src="labelRenderer.js"></script>
            <script type="text/javascript" src="events.js"></script>
            <script type="text/javascript" src="sigma.js/src/misc/sigma.misc.bindEvents.js"></script>
            <script type="text/javascript" src="otherFunctions.js"></script>;
            </div>;
    },
});

module.exports = injectIntl(Sigma);