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
    var temp = 0,
        tarr = [];

    if (node.inParams.length == 0 || node.inParams[0] == " ")
    {
      node.inParams[0] = " ";
    }

    for (i = 0; i < inParams.length; i++){
        temp = 0;
        tarr = [];
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
          tarr.push(
            rectX,
            rectY,
            rectX + rectWidth,
            rectY + rectHeight
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
            tarr.push(
            rectX,
            rectY,
            rectX + rectWidth,
            rectY + rectHeight
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
            tarr.push(
            rectX,
            rectY,
            rectX + rectWidth,
            rectY + rectHeight
          );
          }

         inParamsZones[i] = tarr;
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
              (nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength )) + (nodeWidth * outParams[i].length / allOutsLength),
              (nodeYY + nodeHeight) + size * 0.9
              )
            /*tarr.push(
              nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength ),
              nodeYY + nodeHeight,
              (nodeXX + temp + (nodeWidth * outParams[i-1].length / allOutsLength )) + (nodeWidth * outParams[1].length / allOutsLength),
              nodeYY + nodeHeight + (size * 0.9)
            );*/
            
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
  };;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  return renderer;
}) ();