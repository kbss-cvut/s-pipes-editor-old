'use strict';
import {OverlayTrigger, Popover} from "react-bootstrap";

const React = require('react');

const ModuleTypeList = React.createClass({

    render: function () {
        let listCls = this.props.options.length < 21 ? 'autocomplete-results' : 'autocomplete-results extended';
        if (this.props.customClasses.results)
            listCls += ' ' + this.props.customClasses.results;
        const items = [];
        for (let i = 0, len = this.props.options.length; i < len; i++) {
            let option = this.props.options[i],
                onClick = this.onClick.bind(this, option);
            let className = "fa fa-" + (option["http://topbraid.org/sparqlmotion#icon"] === undefined ? "gear" : option["http://topbraid.org/sparqlmotion#icon"]);
            let popover = (
                <Popover
                    bsClass="module-type popover"
                    id={option["@id"]}
                    key={option["@id"]}
                    title={option["@id"]}>
                    {option["http://www.w3.org/2000/01/rdf-schema#comment"]}
                </Popover>);
            items.push(<li className='btn-link module-type item'
                           key={'typeahead-result-' + i}
                           title={option.description}
                           onClick={onClick}>
                <OverlayTrigger
                    placement="right"
                    key={option["@id"]}
                    overlay={popover}>
                    <span>
                        <i className={className} aria-hidden="true"/>
                        {" " + this.getOptionLabel(option)}
                    </span>
                </OverlayTrigger>
            </li>);
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

export default ModuleTypeList;