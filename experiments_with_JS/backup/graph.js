

sigma.utils.pkg('sigma.canvas.nodes');
sigma.utils.pkg('sigma.canvas.labels');

//////////////////////////////////////////
/////  creating new shape renderer
///////////////////////////////////////////
sigma.canvas.nodes.def = (function() {
  var renderer = function(node, context, settings) {
    var prefix = settings('prefix') || '',
        url = node.url,
        textWidth,
        inParams = node.inParams,
        outParams = node.outParams;
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

    console.log(
    "textWidthLabel=",
    textWidthLabel,
    " allInsLength=",
    allInsLength,
    " allOutsLength=",
    allOutsLength,
    " textWidth=",
    textWidth
  );

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
    context.closePath();
    context.fill();
    context.stroke();

    //draw upper rect(s)
    
    var temp = 0;
    for (i = 0; i < inParams.length; i++){
        temp = 0;
        context.fillStyle = node.color;
        context.beginPath();
        if (i == 0) {
          context.rect(
            nodeXX,
            nodeYY - (size * 0.9),
            nodeWidth * inParams[0].length / allInsLength,
            size * 0.9
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
            context.rect(
              nodeXX + ( nodeWidth * inParams[i-1].length / allInsLength ),
              nodeYY - (size * 0.9),
              nodeWidth * inParams[1].length / allInsLength,
              size * 0.9
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
          else {
            for (j = 0; j < i-1; j++){
              temp += nodeWidth * inParams[j].length / allInsLength; // add width of jst rect
            }
            context.rect(
              nodeXX + temp + (nodeWidth * inParams[i-1].length / allInsLength ),
              nodeYY - (size * 0.9),
              nodeWidth * inParams[i].length / allInsLength,
              size * 0.9
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
    }

    // draw bottom rect(s)
    
    var temp = 0;
    

    for (i = 0; i < outParams.length; i++){
        temp = 0;
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
          }
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
    

    var id = 0,
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
          id: 'n' + id,
          label: 'ahjbfgvjhgfvbhbvjfhbvkfhbvjfbvdfhkbvfghbv' + id,
          x: 0 + id/5,
          y: 0+ id/5,
          size: 15,
          color: '#f5ceca',
          url: 'img1',
          forceLabel: true,
          inParams: ['in1', 'i', 'innn3'],
          outParams: ['out11111', 'out2', 'o3', 'ooo4', '5'],
        }); 
        break

        case 2:
        s.graph.addNode ({
          // Main attributes:
          id: 'n' + id,
          label: 'nodeeee' + id,
          x: 0 + id/5,
          y: 0+ id/5,
          size: 15,
          color: '#dfcde0',
          url: 'img2',
          inParams: ['in1', 'in2', 'in3'],
          outParams: ['out1', 'out22222'],
        });
        break

        case 3:
        s.graph.addNode ({
          // Main attributes:
          id: 'n' + id,
          label: 'n' + id,
          x: 0 + id/5,
          y: 0+ id/5,
          size: 15,
          color: '#e5f98d',
          url: 'img3',
          inParams: ['in1111111', 'innnnnnnn2', 'in33', '4444444444444444444444444444444444444444444444'],
          outParams: ['out1'],
        });
        break
      }
      console.log("Added: ", s.graph.nodes()[id]);
      id++;
      
      // this block is needed because of canvas bug - text in 'inParams' and 'outParams' will behave is strange way without this code
      if (id == 1)
      {
        s.graph.addNode ({
          // Main attributes:
          id: 1488,
          label: ' ',
          x: 0,
          y: 0,
          size: 15,
          color: '#e5f98d',
          url: 'img3',
          inParams: ['in1', 'in2', 'in3', 'in4'],
          outParams: ['out1'],
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

    var dragListener = sigma.plugins.dragNodes(s, s.renderers[0]);
    dragListener.bind('drop', function(event) {
      console.log('Moved: ', event['data']['node']['id'], ' to x =', event['data']['node']['x'], '  y =', event['data']['node']['y']);
    });

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
      document.body.style.cursor = 'move';
    });
    s.bind('outNode', function(e) {
      document.body.style.cursor = 'default';
    });

    CustomShapes.init(s);
    s.refresh();