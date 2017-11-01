import * as React from "react";
import {OverlayTrigger, Popover} from "react-bootstrap";
import {DragSource} from 'react-dnd';
import PropTypes from 'prop-types';


const moduleTypeSource = {
    beginDrag(props) {
        return {};
    }
};

function collect(connect, monitor) {
    return {
        connectDragSource: connect.dragSource(),
        isDragging: monitor.isDragging()
    }
}

class ModuleType extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const option = this.props.option;
        const popover = (
            <Popover
                bsClass="module-type popover"
                id={option["@id"]}
                key={option["@id"]}
                title={option["@id"]}>
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
        </li>
    };
}

ModuleType.propTypes = {
    connectDragSource: PropTypes.func.isRequired,
    isDragging: PropTypes.bool.isRequired
};

export default DragSource('moduleType', moduleTypeSource, collect)(ModuleType);