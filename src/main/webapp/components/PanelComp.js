'use strict';

var React = require('react');

var injectIntl = require('../utils/injectIntl');

var Panel = React.createClass({


    newnode(type) {
        console.log("New node ", type);
        window.newNode(type);
    },
    makeitbeautiful() {
      window.makeItBeautiful();
    },
    clearall: function() {
        console.log("Cleared all");
        window.clearAll();
    },
    render: function () {
        return <div id="panel">
            <div id="p1">
                <br/>
                <input className="button" type="button" value="Authorization" />
                <br/>
                <input className="button" type="button" value="Save graph" onclick="exportJSON()" />
                <br/>
                <input className="button" type="button" value="Load graph" onclick="importJSON()"/>
                <br/>
                <input className="button" type="button" value="Clear canvas" onClick={this.clearall} />
                <br/>
                <hr/>
            </div>
            <div id="p2">
                <h4>Types of nodes: </h4>
                <input className="button" type="button" value="Add node type 1" onClick={()=>this.newnode(1)} />
                <br/>
                <input className="button" type="button" value="Add node type 2" onClick={()=>this.newnode(2)} />
                <br/>
                <input className="button" type="button" value="Add node type 3" onClick={()=>this.newnode(3)} />
                <br/>
                <br/><br/><br/>
                <input className="button" type="button" value="Make it beautiful" onClick={this.makeitbeautiful} />
                <br/><br/><br/>
            </div>
        </div>

    },
});

module.exports = injectIntl(Panel);