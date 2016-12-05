

sigma.utils.pkg('sigma.canvas.nodes');
sigma.utils.pkg('sigma.canvas.edges');
sigma.utils.pkg('sigma.canvas.labels');

//////////////////////////////////////////
/////  creating new node renderer
///////////////////////////////////////////
sigma.canvas.nodes.def = (function() {
  var renderer = function(node, context, settings) {
    var prefix = settings('prefix') || '',
        url = node.url,
        textWidth,
        inParams = node.inParams,
        outParams = node.outParams;
        inParamsZones = node.inParamsZones;
        outParamsZones = node.outParamsZones;
    size = node[prefix + 'size'];
    textWidth = context.measureText(node.label).width;

    textWidthLabel = context.measureText(node.label).width;

    var allInsLength = 0;
    for (i = 0; i < inParams.length; i++)
      allInsLength += inParams[i].length;

    var allOutsLength = 0;
    for (i = 0; i < outParams.length; i++)
      allOutsLength += outParams[i].length;

    if (textWidthLabel > allInsLength*6 && textWidthLabel > allOutsLength*6)
      textWidth = textWidthLabel;
    else if (textWidthLabel > allInsLength*6 && textWidthLabel < allOutsLength*6)
      textWidth = allOutsLength*6;
    else if (textWidthLabel < allInsLength*6 && textWidthLabel > allOutsLength*6)
      textWidth = allOutsLength*6;
    else if (textWidthLabel < allInsLength*6 && textWidthLabel < allOutsLength*6 && allInsLength < allOutsLength)
      textWidth = allOutsLength*6;
    else if (textWidthLabel < allInsLength*6 && textWidthLabel < allOutsLength*6 && allOutsLength < allInsLength)
      textWidth = allInsLength*6;

    var nodeXX = (node[prefix + 'x'] - (textWidth * 1.2) * 0.5) - 50,
        nodeYY = node[prefix + 'y'] - size * 1.2 * 0.8,
        nodeWidth = textWidth * 1.2 + 50 * 2,
        nodeHeight = size * 1.2 * 1.6;

    context.fillStyle = node.color;
    //context.strokeStyle = node.color || settings('defaultNodeColor');
    context.strokeStyle = 'black';
    context.lineWidth = size * 0.1;


    //draw mid rect
    context.beginPath();
      context.rect(
        nodeXX,
        nodeYY,
        nodeWidth,
        nodeHeight
      ); 
    node.coordinates[0] = nodeXX;
    node.coordinates[1] = nodeYY;
    node.coordinates[2] = nodeXX + nodeWidth;
    node.coordinates[3] = nodeYY + nodeHeight;
    context.closePath();
    context.fill();
    context.stroke();

    var rectX, 
        rectY, 
        rectWidth, 
        rectHeight;

    //draw upper rect(s)
    var temp = 0;
    for (i = 0; i < inParams.length; i++){
        temp = 0;
        context.fillStyle = node.color;
        context.beginPath();

        if (i == 0) {
          rectX = nodeXX;
          rectY = nodeYY - (size * 0.9);
          rectWidth = nodeWidth * inParams[0].length / allInsLength;
          rectHeight = size * 0.9;
          context.rect(
            rectX,
            rectY,
            rectWidth,
            rectHeight
          );
          context.closePath();
          context.fill();
          context.stroke();
          context.fillStyle = "#000";
          context.fillText(
            inParams[i],
            nodeXX + (nodeWidth * inParams[0].length / allInsLength) * 0.5,
            nodeYY - (size * 0.2)
          );

        } else if (i == 1){
            rectX = nodeXX + ( nodeWidth * inParams[i-1].length / allInsLength );
            rectY = nodeYY - (size * 0.9);
            rectWidth = nodeWidth * inParams[1].length / allInsLength;
            rectHeight = size * 0.9;
            context.rect(
              rectX,
              rectY,
              rectWidth,
              rectHeight
            );
            context.closePath();
            context.fill();
            context.stroke();
            context.fillStyle = "#000";
            context.fillText(
              inParams[i],
              nodeXX + ( nodeWidth * inParams[i-1].length / allInsLength ) + (nodeWidth * inParams[1].length / allInsLength) * 0.5,
              nodeYY - (size * 0.2)
            );
          }
          else  if (i > 1){
            for (j = 0; j < i-1; j++){
              temp += nodeWidth * inParams[j].length / allInsLength; // add width of jst rect
            }
            rectX = nodeXX + temp + (nodeWidth * inParams[i-1].length / allInsLength );
            rectY = nodeYY - (size * 0.9);
            rectWidth = nodeWidth * inParams[i].length / allInsLength;
            rectHeight = size * 0.9;
            context.rect(
              rectX,
              rectY,
              rectWidth,
              rectHeight
            );
            context.closePath();
            context.fill();
            context.stroke();
            context.fillStyle = "#000";
            context.fillText(
              inParams[i],
              nodeXX + temp + (nodeWidth * inParams[i-1].length / allInsLength) + ((nodeWidth * inParams[i].length / allInsLength)*0.5),
              nodeYY - (size * 0.2)
            );
          }
          if (inParamsZones.length ==0)
           node.inParamsZones.push(
            rectX,
            rectY,
            rectX + rectHeight,
            rectY + rectHeight
          );
    }
    

    // draw bottom rect(s)
    
    var temp = 0,
        tarr = [];
    

    for (i = 0; i < outParams.length; i++){
        temp = 0;
        tarr = [];
        context.fillStyle = node.color;
        context.beginPath();
        if (i == 0) {
          context.rect(
            nodeXX,
            nodeYY + nodeHeight,
            nodeWidth * outParams[0].length / allOutsLength,
            size * 0.9
          );
          context.closePath();
          context.fill();
          context.stroke();
          context.fillStyle = "#000";
          context.fillText(
            outParams[i],
            nodeXX + (nodeWidth * outParams[0].length / allOutsLength) * 0.5,
            nodeYY + nodeHeight + 10.5
          );

          tarr.push(
            nodeXX,
            nodeYY + nodeHeight,
            nodeXX + (nodeWidth * outParams[0].length / allOutsLength),
            nodeYY + nodeHeight + (size * 0.9)
          );
        } else if (i == 1){
            context.rect(
              nodeXX + ( nodeWidth * outParams[i-1].length / allOutsLength ),
              nodeYY + nodeHeight,
              nodeWidth * outParams[1].length / allOutsLength,
              size * 0.9
            );
            context.closePath();
            context.fill();
            context.stroke();
            context.fillStyle = "#000";
            context.fillText(
              outParams[i],
              nodeXX + ( nodeWidth * outParams[i-1].length / allOutsLength ) + (nodeWidth * outParams[1].length / allOutsLength) * 0.5,
              nodeYY + nodeHeight + 10.5
            );

            tarr.push(
              nodeXX + (nodeWidth * outParams[i-1].length / allOutsLength ),
              nodeYY + nodeHeight,
              nodeXX + ( nodeWidth * outParams[i-1].length / allOutsLength ) + (nodeWidth * outParams[1].length / allOutsLength),
              nodeYY + nodeHeight + (size * 0.9)
            );
          
          }
          else {
            for (j = 0; j < i-1; j++){
              temp += nodeWidth * outParams[j].length / allOutsLength; // add width of jst rect
            }
            context.rect(
              nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength ),
              nodeYY + nodeHeight,
              nodeWidth * outParams[i].length / allOutsLength,
              size * 0.9
            );
            context.closePath();
            context.fill();
            context.stroke();
            context.fillStyle = "#000";
            context.fillText(
              outParams[i],
              nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength) + ((nodeWidth * outParams[i].length / allOutsLength)*0.5),
              nodeYY+ nodeHeight + 10.5
            );
            tarr.push(
              nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength ),
              nodeYY + nodeHeight,
              nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength ) + (nodeWidth * outParams[1].length / allOutsLength),
              nodeYY + nodeHeight + (size * 0.9)
            );
          }
      //if (outParamsZones.length < outParams.length)
        outParamsZones[i] = tarr;
    }

    // draw image
    context.drawImage(
      document.getElementById(url),
      ((node[prefix + 'x'] - (textWidth * 1.2) * 0.5) - 50) + (textWidth * 1.2 + 50 * 2) - size * 1.9,
      node[prefix + 'y'] - 12,
      size * 1.6,
      size * 1.6
    );
}

  return renderer;
}) ();


//////////////////////////////////////////
/////  creating new edge renderer
///////////////////////////////////////////
sigma.canvas.edges.def = function(edge, source, target, context, settings) {
  var color = 'black',
      prefix = settings('prefix') || '',
      edgeColor = settings('edgeColor'),
      defaultNodeColor = settings('defaultNodeColor'),
      defaultEdgeColor = settings('defaultEdgeColor');

  context.strokeStyle = color;
  context.lineWidth = 1;
  context.beginPath();
  edge.coordinates = [
  (s.graph.nodes()[edge.source].outParamsZones[edge.sourceZone][0] + s.graph.nodes()[edge.source].outParamsZones[edge.sourceZone][2]) / 2,
   s.graph.nodes()[edge.source].outParamsZones[edge.sourceZone][3],
  (s.graph.nodes()[edge.target].coordinates[0] + s.graph.nodes()[edge.target].coordinates[2]) / 2 ,
   s.graph.nodes()[edge.target].coordinates[1] - 15*0.9
  ]

  context.moveTo(
    edge.coordinates[0],
    edge.coordinates[1]
  );

  for (i = 2; i < edge.coordinates.length; i+=2)
  {
    context.lineTo(
      edge.coordinates[i],
      edge.coordinates[i+1]
    );
  }

/*  context.lineTo(
    coordinates[coordinates.length-2],
    coordinates[coordinates.length-1]
  );*/

  context.closePath();
  context.stroke();

};

//////////////////////////////////////////
/////  creating new label renderer for previously defined shape renderer
///////////////////////////////////////////
sigma.canvas.labels.def = function(node, context, settings) {
    // declarations
    var prefix = settings('prefix') || '';
    var size = node[prefix + 'size'];
    var nodeX = node[prefix + 'x'];
    var nodeY = node[prefix + 'y'];
    // measure text width
    var textWidth = context.measureText(node.label).width;
    // define settings
    context.fillStyle = 'black'; //node.textColor;
    context.lineWidth = size * 0.1;
    context.textAlign = 'center';
    context.fillText(
      node.label, 
      nodeX - size * 1.6,
      nodeY + 4
    );
};
    

    var idN = 0,
        idE = 0,
        g = {
          nodes: [],
          edges: []
        };

    function triggerMouseEvent (node, eventType) {
      var clickEvent = document.createEvent ('MouseEvents');
      clickEvent.initEvent (eventType, true, true);
      node.dispatchEvent (clickEvent);
    }

    function newNode(typeOfNode)
    {
      switch (typeOfNode)
      {
        case 1:
        s.graph.addNode ({
          // Main attributes:
          id: idN,
          label: 'ahjbfgvjhgfvbhbvjfhbvkfhbvjfbvdfhkbvfghbv' + idN,
          x: 0 + idN/5,
          y: 0 + idN/5,
          coordinates: [],
          size: 15,
          color: '#f5ceca',
          url: 'img1',
          forceLabel: true,
          inParams: [],
          inParamsZones: [],
          outParams: ['out11111', 'out2', 'o3', 'ooo4', '5'],
          outParamsZones: [],
        }); 
        var arr = [];
        
        break

        case 2:
        s.graph.addNode ({
          // Main attributes:
          id: idN,
          label: 'nodeeee' + idN,
          x: 0 + idN/5,
          y: 0 + idN/5,
          coordinates: [],
          size: 15,
          color: '#dfcde0',
          url: 'img2',
          inParams: [],
          inParamsZones: [],
          outParams: ['out1', 'out22222'],
          outParamsZones: []
        });
        break

        case 3:
        s.graph.addNode ({
          // Main attributes:
          id: idN,
          label: 'n' + idN,
          x: 0 + idN/5,
          y: 0 + idN/5,
          coordinates: [],
          size: 15,
          color: '#e5f98d',
          url: 'img3',
          inParams: [],
          inParamsZones: [],
          outParams: ['out1'],
          outParamsZones: [],
        });
        break
      }
      console.log("Added ", s.graph.nodes()[idN]);
      idN++; 
      s.refresh();
      
      // this block is needed because of canvas bug - text in 'inParams' and 'outParams' will behave is strange way without this code
      if (idN == 1)
      {
        s.graph.addNode ({
          // Main attributes:
          id: 1488,
          label: ' ',
          x: 0,
          y: 0,
          coordinates: [],
          size: 15,
          color: '#e5f98d',
          url: 'img3',
          inParams: [],
          inParamsZones: [],
          outParams: [],
          outParamsZones: []
        });
        s.refresh();
        s.graph.dropNode(1488);
        s.refresh();
      }
      s.refresh();
      
    }

    
    s = new sigma({
      graph: g,
      renderer: {
        container: document.getElementById('container'),
        type: 'canvas',
      },
      settings: {
         minNodeSize: 1,
         maxNodeSize: 15,
         mouseWheelEnabled: false,
         doubleClickEnabled: false,
         enableHovering: false,
         labelThreshold: 70
      }
    });

    //get the popup box
    var modal = document.getElementById('myModal');
    var drawingEdge = false;

    var dragListener = sigma.plugins.dragNodes(s, s.renderers[0]);
    dragListener.bind('drop', function(event) {
      console.log('Now: x ', s.graph.nodes()[0]);
      drawingEdge = false;
    });

    
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
          y > s.graph.nodes()[node].coordinates[1] && y < s.graph.nodes()[node].coordinates[3])
        {
            return [true, node];
        }
      }
      return [false];
    } 
    var canvas = document.getElementsByClassName('sigma-mouse')[0];

    var     edgeStartNode,
            edgeEndNode, 
            edgeStartX, 
            edgeStartY,
            edgeEndX,
            edgeEndY;

    canvas.addEventListener('click', function(evt) {
        
        //edgeStartNode = ifOnOutParamsZones(canvas, evt);
        //edgeEndNode = ifOnNode(canvas, evt);
        if (drawingEdge == false && ifOnOutParamsZones(canvas, evt)[0] == true) // user started drawing edge
        {
            edgeStartNode = ifOnOutParamsZones(canvas, evt);
            console.log("startEdge ", edgeStartNode);
            drawingEdge = true;                      
            document.body.style.cursor = 'crosshair';
            edgeStartX = (s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][0] + s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][2]) / 2;
            edgeStartY = s.graph.nodes()[edgeStartNode[1]].outParamsZones[edgeStartNode[2]][3];
        }
        else if (drawingEdge == true)
        {
            
            edgeEndNode = ifOnNode(canvas, evt);
            if (edgeEndNode[0] == true) //if drawing edge ended on node
            {
              drawingEdge = false;
              document.body.style.cursor = 'default';
              s.graph.addEdge({
                id: idE,
                source: s.graph.nodes()[edgeStartNode[1]].id,
                sourceZone: edgeStartNode[2],
                target: s.graph.nodes()[edgeEndNode[1]].id,
                coordinates: [],
              });
              console.log("Added edge: ", s.graph.edges()[idE]);
              s.graph.nodes()[s.graph.nodes()[edgeEndNode[1]].id].inParams.push( s.graph.nodes()[s.graph.nodes()[edgeStartNode[1]].id].outParams[edgeStartNode[2]] );
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
/*        edgeEndNode = 0;
        edgeStartX = 0;
        edgeStartY = 0;
        edgeEndX = 0;
        edgeEndY = 0;*/
      }, true);

    


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
    }

    s.bind('overNode', function(e) {
      if (drawingEdge == false ) document.body.style.cursor = 'move';
    });
    s.bind('outNode', function(e) {
      if (drawingEdge == false ) document.body.style.cursor = 'default';
    });

    
    

    
    CustomShapes.init(s);
    s.refresh();