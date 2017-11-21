import React from 'react';
import {Pagination} from 'react-bootstrap';

import {FormattedMessage} from 'react-intl';

import Actions from '../../actions/Actions';
import ComponentStateStore from '../../stores/ComponentStateStore';

const MAX_BUTTONS = 5;

/**
 * Mixin for data paging in tables.
 *
 * It is compimport {} from ;ely self-contained and provides methods which:
 * <ul>
 *     <li>Render the pagination component when necessary (when data don't fit one page)</li>
 *     <li>Return portion of data corresponding to the currently active page</li>
 * </ul>
 */
const PagingMixin = {

    propTypes: {
        pageSize: React.PropTypes.number,
        maxButtons: React.PropTypes.number
    },

    getDefaultProps: function () {
        return {
            pageSize: 11,
            maxButtons: MAX_BUTTONS
        }
    },

    getInitialState: function () {
        let page = 1;
        if (this.getDisplayName) {
            const state = ComponentStateStore.getComponentState(this.getDisplayName());
            if (state && state.activePage) {
                page = state.activePage;
            }
        }
        return {activePage: page};
    },

    _onPageSelect: function (e, selectedEvent) {
        const page = selectedEvent.eventKey;
        this.setState({activePage: page});
        if (this.getDisplayName) {
            Actions.rememberComponentState(this.getDisplayName(), {activePage: page});
        }
    },

    /**
     * Resets pagination to defaults.
     */
    resetPagination: function () {
        this.setState(this.getInitialState());
        if (this.getDisplayName) {
            Actions.rememberComponentState(this.getDisplayName(), this.getInitialState());
        }
    },

    /**
     * Assuming that the data is an array, this function returns a sub-array of the data corresponding to the currently
     * active page.
     * @param data array of data
     */
    getCurrentPage: function (data) {
        const startIndex = this.props.pageSize * (this.state.activePage - 1),
            endIndex = startIndex + this.props.pageSize > data.length ? data.length : startIndex + this.props.pageSize;
        return data.slice(startIndex, endIndex);
    },

    _renderCountInfo: function (data) {
        const currentItemCnt = this.getCurrentPage(data).length;
        return <div className='paging-item-count-info'>
            <FormattedMessage id='reports.paging.item-count' values={{showing: currentItemCnt, total: data.length}}/>
        </div>;
    },

    /**
     * Renders the pagination component.
     *
     * If the data fits one page, {@code null} is returned, as there is no need for paging.
     * @param data The data that will be shown and need paging
     */
    renderPagination: function (data) {
        const itemCount = Math.ceil(data.length / this.props.pageSize);
        if (itemCount === 1) {
            return <div>
                {this._renderCountInfo(data)}
            </div>;
        }
        return <div>
            <Pagination
                prev next first last ellipsis boundaryLinks items={itemCount} maxButtons={this.props.maxButtons}
                activePage={this.state.activePage} onSelect={this._onPageSelect}/>
            {this._renderCountInfo(data)}
        </div>;
    }
};

module.exports = PagingMixin;
