/**
 * @jsx
 */

'use strict';

let React = require('react');
let Jumbotron = require('react-bootstrap').Jumbotron;
let Grid = require('react-bootstrap').Grid;
let Col = require('react-bootstrap').Col;
let Row = require('react-bootstrap').Row;

let injectIntl = require('../../utils/injectIntl');
let FormattedMessage = require('react-intl').FormattedMessage;

let Authentication = require('../../utils/Authentication');
let Constants = require('../../constants/Constants');
let Tile = require('./DashboardTile').default;
let I18nMixin = require('../../i18n/I18nMixin');

let Dashboard = React.createClass({
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
                    <Tile onClick={this.props.handlers.showUsers}>{this.i18n('dashboard.users-tile')}</Tile>
                </Col>
                <Col xs={3} className='dashboard-sector'>
                    <Tile onClick={this.props.handlers.showViews}>{this.i18n('dashboard.views-tile')}</Tile>
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
