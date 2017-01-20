import { Button } from 'react-bootstrap';
import { Configuration, WizardGenerator } from "semforms";
import I18nStore from "./stores/I18nStore";
import WizardStore from "./stores/WizardStore";
import RecordController from "./components/record/RecordController";

var addLocaleData = require('react-intl').addLocaleData;
var intlData = require('./i18n/en');

var IntlProvider = require('react-intl').IntlProvider;

I18nStore.setMessages(intlData.messages);




var callback2  = (prop)=>{
    console.log("prop: ", prop);
};

Configuration.wizardStore = WizardStore;
Configuration.intl = I18nStore.getIntl();

console.log("NOW");
var wiz = WizardGenerator.createDefaultWizard(null, null, callback2);




var App = React.createClass({
    render: function () {
        return (
            <IntlProvider {...intlData}>
            <div>
                <RecordController/

<div id="myModal" className="modal">

    <div className="modal-content">
        <div className="modal-header">
            <h3>Modal Header</h3>
        </div>
        <div className="modal-body">
            <ul className="tab">
                <li><a href="javascript:void(0)" className="tablinks" onclick="openCity(event, 'London')">London</a></li>
                <li><a href="javascript:void(0)" className="tablinks" onclick="openCity(event, 'Paris')">Paris</a></li>
                <li><a href="javascript:void(0)" className="tablinks" onclick="openCity(event, 'Tokyo')">Tokyo</a></li>
            </ul>

            <div id="London" className="tabcontent">
            <h3>London</h3>
            <p>London is the capital city of England.</p>
            </div>

            <div id="Paris" className="tabcontent">
            <h3>Paris</h3>
            <p>Paris is the capital of France.</p>
            </div>

            <div id="Tokyo" className="tabcontent">
            <h3>Tokyo</h3>
            <p>Tokyo is the capital of Japan.</p>
            </div>
        <br/>
        </div>

    </div>

</div>


    <div id="container"></div>

    <script type="text/javascript" src="modal.js"></script>
    <script type="text/javascript" src="graph.js"></script>
    <script type="text/javascript" src="nodeRenderer.js"></script>
    <script type="text/javascript" src="edgeRenderer.js"></script>
    <script type="text/javascript" src="labelRenderer.js"></script>
    <script type="text/javascript" src="events.js"></script>
    <script type="text/javascript" src="sigma.js/src/misc/sigma.misc.bindEvents.js"></script>
    <script type="text/javascript" src="otherFunctions.js"></script>

    <div id="panel">
<div id="p1">
<br/>
<input className="button" type="button" value="Authorization" />
<br/>
<input className="button" type="button" value="Save graph" onclick="exportJSON()" />
    <Button onClick={window.newNode(1)}>Default</Button>
<br/>
<input className="button" type="button" value="Load graph" onclick="importJSON()"/>
<br/>
<input className="button" type="button" value="Clear canvas" onclick="clearAll()" />
<br/>
<hr/>
</div>


<div id="p2">
<h4>Types of nodes: </h4>
    <input className="button" type="button" value="Add node type 1" onСlick={window.newNode(1)} />
<br/>
<input className="button" type="button" value="Add node type 2" onclick="newNode(2)" />
<br/>
<input className="button" type="button" value="Add node type 3" onclick="newNode(3)" />
<br/>
<br/><br/><br/>
<input className="button" type="button" value="Make it beautiful" onclick="makeItBeautiful();" />
<br/>





<br/>
<br/>
</div>
</div>

<script type="text/javascript" src="popup.js"></script>
</div>


                </IntlProvider>
        );
    }
});

ReactDOM.render(<App />, document.getElementById('root'));
