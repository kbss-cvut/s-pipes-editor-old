import * as React from "react";

class ModuleType extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <li className='btn-link item' key={this.props.key} title={this.props.title}
                   onClick={this.props.onClick}>{this.props.value}</li>
    }
}

export default ModuleType;