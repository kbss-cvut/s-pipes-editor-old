'use strict';
import React from 'react';
import Function from "../moduleType/Function";

const FunctionList = React.createClass({

    render: function () {
        let listCls = this.props.options.length < 21 ? 'autocomplete-results' : 'autocomplete-results extended';
        if (this.props.customClasses.results)
            listCls += ' ' + this.props.customClasses.results;
        const items = [];
        for (let i = 0, len = this.props.options.length; i < len; i++) {
            let option = this.props.options[i],
                onClick = this.onClick.bind(this, option);
            let className = "fa fa-play-circle";

            items.push(
                <Function
                    key={'typeahead-result-' + i}
                    onClick={onClick}
                    option={option}
                    className={className}
                    value={this.getOptionLabel(option)}/>
            );
        }
        return <ul className={listCls}>
            {items}
        </ul>;
    },

    getOptionLabel: function (option) {
        if (typeof this.props.displayOption === 'function') {
            return this.props.displayOption(option);
        } else {
            return option[this.props.displayOption];
        }
    },

    onClick: function (option, event) {
        event.preventDefault();
        this.props.onOptionSelected(option, event);
    }
});

export default FunctionList;