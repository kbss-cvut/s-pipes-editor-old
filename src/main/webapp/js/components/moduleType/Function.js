import * as React from 'react';
import {OverlayTrigger, Popover} from "react-bootstrap";

const FUNCTION_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-function-uri";

class Function extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const option = this.props.option;
        const popover = (
            <Popover
                bsClass="module-type popover"
                id={option[FUNCTION_URI]}
                key={option[FUNCTION_URI]}
                title={option[FUNCTION_URI]}>
                {option["http://www.w3.org/2000/01/rdf-schema#comment"]}
            </Popover>);
        return <li
            className='btn-link module-type item'
            title={option.description}
            onClick={this.props.onClick}>
            <OverlayTrigger
                placement="right"
                key={option["@id"]}
                overlay={popover}>
            <span>
                <i className={this.props.className} aria-hidden="true"/>
                {" " + this.props.value}
            </span>
            </OverlayTrigger>
        </li>;
    };
}

export default Function;