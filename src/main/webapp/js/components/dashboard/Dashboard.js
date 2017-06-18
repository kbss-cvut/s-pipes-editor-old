/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Authentication = require('../../utils/Authentication');
var Constants = require('../../constants/Constants');
var Tile = require('./DashboardTile').default;
var I18nMixin = require('../../i18n/I18nMixin');

var Dashboard = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        userFirstName: React.PropTypes.string,
        dashboard: React.PropTypes.string,
        handlers: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            dashboard: this.props.dashboard ? this.props.dashboard : Constants.DASHBOARDS.MAIN.id,
            search: false
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    goBack: function () {
        this.setState({dashboard: Constants.DASHBOARD_GO_BACK[this.state.dashboard]});
    },

    toggleSearch: function () {
        this.setState({search: !this.state.search});
    },


    render: function () {
        return (
            <div style={{margin: '0 -15px 0 -15px'}}>
                <div className='col-xs-10'>
                    <Jumbotron>
                        {this.renderTitle()}
                        {this.renderDashboardContent()}
                    </Jumbotron>
                </div>
            </div>

        );
    },

    renderTitle: function () {
        return <h3><FormattedMessage id='dashboard.welcome'
                                     values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
        </h3>;
    },

    renderDashboardContent: function () {
        return this._renderMainDashboard();
    },

    _renderMainDashboard: function () {
        return <Grid fluid={true}>
            <Row>
                <Col xs={3} className='dashboard-sector'>
                    <Tile onClick={this.props.handlers.createRecord}>{this.i18n('dashboard.create-tile')}</Tile>
                </Col>
                {this._renderUsersTile()}
                <Col xs={3} className='dashboard-sector'>
                    <Tile onClick={this.props.handlers.showRecords}>{this.i18n('dashboard.records-tile')}</Tile>
                </Col>
            </Row>
        </Grid>;
    },

    _renderUsersTile: function () {
        return Authentication.isAdmin() ?
            <Col xs={3} className='dashboard-sector'>
                <Tile onClick={this.props.handlers.showUsers}>{this.i18n('dashboard.users-tile')}</Tile>
            </Col> : null;
    }
});

module.exports = injectIntl(Dashboard);
