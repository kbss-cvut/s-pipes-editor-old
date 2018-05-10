import * as React from 'react';

class Function extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const option = this.props.option;
        return <li
            className='btn-link module-type item'
            title={option.description}
            onClick={this.props.onClick}>
            <span>
                <i className={this.props.className} aria-hidden="true"/>
                {" " + this.props.value}
            </span>
        </li>
    };
}

export default Function;