'use strict';

import React from 'react';
import {Col, Grid, Jumbotron, Row} from 'react-bootstrap';
import injectIntl from '../../utils/injectIntl';
import Constants from '../../constants/Constants';
import I18nMixin from '../../i18n/I18nMixin';
import {FormattedMessage} from 'react-intl';
import {default as Tile} from './DashboardTile'
import PropTypes from 'prop-types';

let Dashboard = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        dashboard: PropTypes.string,
        handlers: PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            dashboard: this.props.dashboard ? this.props.dashboard : Constants.DASHBOARDS.MAIN.id,
        }
    },

    goBack: function () {
        this.setState({dashboard: Constants.DASHBOARD_GO_BACK[this.state.dashboard]});
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
        return <h3><FormattedMessage id='dashboard.welcome'/>
        </h3>;
    },

    renderDashboardContent: function () {
        return <Grid fluid={true}>
            <Row>
                <Col xs={3} className='dashboard-sector'>
                    <Tile onClick={this.props.handlers.showScripts}>{this.i18n('dashboard.views-tile')}</Tile>
                </Col>
            </Row>
        </Grid>;
    },
});

module.exports = injectIntl(Dashboard);