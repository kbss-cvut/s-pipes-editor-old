


function triggerMouseEvent (node, eventType) {
  var clickEvent = document.createEvent ('MouseEvents');
  clickEvent.initEvent (eventType, true, true);
  node.dispatchEvent (clickEvent);
}

function ifOnOutParamsZones(canvas, evt)
{
  var rect = canvas.getBoundingClientRect();
  var x = evt.clientX - rect.left,
      y = evt.clientY - rect.top;
  for (var node = 0; node < s.graph.nodes().length; node++)
  {
    for (var zone = 0; zone < s.graph.nodes()[node].outParamsZones.length; zone++)
    {
      if(x > s.graph.nodes()[node].outParamsZones[zone][0] && x < s.graph.nodes()[node].outParamsZones[zone][2] && 
        y > s.graph.nodes()[node].outParamsZones[zone][1] && y < s.graph.nodes()[node].outParamsZones[zone][3])
      {
          return [true, node, zone, x, y];
      }
    }
  }
  return [false];
} 

function ifOnNode(canvas, evt)
{
  var rect = canvas.getBoundingClientRect();
  var x = evt.clientX - rect.left,
      y = evt.clientY - rect.top;
  for (var node = 0; node < s.graph.nodes().length; node++)
  {
    if(x > s.graph.nodes()[node].coordinates[0] && x < s.graph.nodes()[node].coordinates[2] && 
      y > s.graph.nodes()[node].coordinates[1]-15 && y < s.graph.nodes()[node].coordinates[3])
    {
        return [true, node];
    }
  }
  return [false];
} 

var canvas = document.getElementsByClassName('sigma-mouse')[0];

var   edgeStartNode,
      edgeEndNode, 
      edgeStartX, 
      edgeStartY,
      edgeEndX,
      edgeEndY;

var drawingEdge = false,
    drawingEdgeWithParametr = false;
    moving = false;

function getMousePos(canvas, evt) {
  var rect = canvas.getBoundingClientRect();
  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

document.getElementsByClassName('sigma-mouse')[0].addEventListener('mouseup', function(evt) {
  if (drawingEdge == false && (ifOnNode(canvas, evt)[0] == true || ifOnOutParamsZones(canvas, evt)[0] == true)) // user started drawing edge
  {
      if (ifOnNode(canvas, evt)[0] == true)
      {
        edgeStartNode = ifOnNode(canvas, evt);
        drawingEdgeWithParametr = false;
        // DRAW NORMAL EDGE WITHOUT PARAMETR
      } 
      else if (ifOnOutParamsZones(canvas, evt)[0] == true)
      {
        edgeStartNode = ifOnOutParamsZones(canvas, evt);
        drawingEdgeWithParametr = true;
        // DRAW EDGE WITH PARAMETR
        
      } 

      console.log("startEdge ", edgeStartNode);
      drawingEdge = true;   

      console.log(getMousePos(canvas, evt).x);  

      document.body.style.cursor = 'crosshair';
      //edgeStartX = (s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][0] + s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][2]) / 2;
      //edgeStartY = s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][3];
  }
  else if (drawingEdge == true && moving == false)
  {
      edgeEndNode = ifOnNode(canvas, evt);
      // s.graph.nodes()[s.graph.nodes()[edgeEndNode[1]].id].inParams.push( s.graph.nodes()[s.graph.nodes()[edgeStartNode[1]].id].outParams[edgeStartNode[2]] );
      s.refresh();
      if (edgeEndNode[0] == true) //if drawing edge ended on node
      {
        drawingEdge = false;
        document.body.style.cursor = 'default';
        s.graph.addEdge({
          id: idE,
          source: s.graph.nodes()[edgeStartNode[1]].id,
          //sourceZone: edgeStartNode[2],
          target: s.graph.nodes()[edgeEndNode[1]].id,
          //targetZone: s.graph.nodes()[s.graph.nodes()[edgeEndNode[1]].id].inParams.length - 1,
          withParam: drawingEdgeWithParametr,
          coordinates: [],
        });
        console.log("Added edge: ", s.graph.edges()[idE]);

        s.refresh();
        idE++;
      }
      else // if drawing ended not on node
      {
        drawingEdge = false;
        document.body.style.cursor = 'default';
        console.log("Ended on nowhere");
      }
  }
}, true);

    




    

//import WizardGenerator from "semforms/src/components/model/WizardGenerator.js";
//import {Configuration, WizardGenerator} from "semforms";

// Open modal
s.bind('doubleClickNode', function(e) {
  modal.style.display = "block"; //show modal
  console.log(e.type, e.data.node.label, e.data.captor);
});

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
};

s.bind('overNode', function(e) {
  if (drawingEdge == false ) document.body.style.cursor = 'move';
  moving = true;
});
s.bind('outNode', function(e) {
  if (drawingEdge == false ) document.body.style.cursor = 'default';
  moving = false;
  drawingEdge = false;
});
