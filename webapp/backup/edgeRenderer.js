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
  //(s.graph.nodes()[edge.target].coordinates[0] + s.graph.nodes()[edge.target].coordinates[2]) / 2 ,
  (s.graph.nodes()[edge.target].inParamsZones[edge.targetZone][0] + s.graph.nodes()[edge.target].inParamsZones[edge.targetZone][2] ) / 2,
   s.graph.nodes()[edge.target].coordinates[1] - 15*0.9
  ];;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  
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

////////////////////////////////////////////////////
  var size = 6,
      tSize = target[prefix + 'size'],
      aSize = Math.max(size * 2.5, settings('minArrowSize')),
      sX = edge.coordinates[0],
      sY = edge.coordinates[1],
      tX = edge.coordinates[edge.coordinates.length-2],
      tY = edge.coordinates[edge.coordinates.length-1],
      d = Math.sqrt(Math.pow(tX - sX, 2) + Math.pow(tY - sY, 2)) * 2,
      aX = sX + (tX - sX) * (d - aSize - tSize) / d,
      aY = sY + (tY - sY) * (d - aSize - tSize) / d,
      vX = (tX - sX) * aSize / d * 2,
      vY = (tY - sY) * aSize / d * 2;

  context.fillStyle = color;
  context.beginPath();
  context.moveTo(aX + vX, aY + vY);
  context.lineTo(aX + vY * 0.4, aY - vX * 0.4);
  context.lineTo(aX - vY * 0.4, aY + vX * 0.4);
  context.lineTo(aX + vX, aY + vY);
  context.closePath();
  context.fill();

};