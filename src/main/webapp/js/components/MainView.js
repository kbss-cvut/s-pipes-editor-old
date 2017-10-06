'use strict';

let React = require('react');
let Reflux = require('reflux');

let Nav = require('react-bootstrap').Nav;
let Navbar = require('react-bootstrap').Navbar;
let NavBrand = require('react-bootstrap').NavbarBrand;
let NavItem = require('react-bootstrap').NavItem;
let NavDropdown = require('react-bootstrap').NavDropdown;
let MenuItem = require('react-bootstrap').MenuItem;
let LinkContainer = require('react-router-bootstrap').LinkContainer;
let injectIntl = require('../utils/injectIntl');

let Actions = require('../actions/Actions');
let Constants = require('../constants/Constants');
let I18nMixin = require('../i18n/I18nMixin');
let I18nStore = require('../stores/I18nStore');

let Authentication = require('../utils/Authentication');
let Routes = require('../utils/Routes');
let UserStore = require('../stores/UserStore');

let MainView = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded'),
        I18nMixin
    ],

    getInitialState: function () {
        return {
            loggedIn: UserStore.isLoaded()
        }
    },

    componentWillMount: function () {
        I18nStore.setIntl(this.props.intl);
    },

    onUserLoaded: function (data) {
        if (data.action === Actions.loadCurrentUser) {
            this.setState({loggedIn: true});
        }
    },

    render: function () {
        if (!this.state.loggedIn) {
            return (<div>{this.props.children}</div>);
        }
        let user = UserStore.getCurrentUser();
        let name = user.firstName.substr(0, 1) + '. ' + user.lastName;
        return (
            <div>
                <header>
                    <Navbar fluid={true}>
                        <NavBrand>{Constants.APP_NAME}</NavBrand>
                        <Nav>
                            <LinkContainer
                                to='dashboard'><NavItem>{this.i18n('main.dashboard-nav')}</NavItem></LinkContainer>
                        </Nav>
                        {this._renderUsers()}
                        <Nav>
                            <LinkContainer
                                to='clinics'><NavItem>{this.i18n('main.clinics-nav')}</NavItem></LinkContainer>
                        </Nav>
                        <Nav>
                            <LinkContainer
                                to='records'><NavItem>{this.i18n('main.records-nav')}</NavItem></LinkContainer>
                        </Nav>
                        <Nav pullRight style={{margin: '0 -15px 0 0'}}>
                            <NavDropdown id='logout' title={name}>
                                <MenuItem
                                    href={'#/' + Routes.users.path + '/' + user.username}>{this.i18n('main.my-profile')}</MenuItem>
                                <MenuItem href='#' onClick={Authentication.logout}>{this.i18n('main.logout')}</MenuItem>
                            </NavDropdown>
                        </Nav>
                    </Navbar>
                </header>
                <section style={{height: '100%'}}>
                    {this.props.children}
                </section>
            </div>
        );
    },

    _renderUsers: function () {
        return Authentication.isAdmin() ?
            <Nav>
                <LinkContainer to='users'><NavItem>{this.i18n('main.users-nav')}</NavItem></LinkContainer>
            </Nav> : null;
    }
});

module.exports = injectIntl(MainView);
