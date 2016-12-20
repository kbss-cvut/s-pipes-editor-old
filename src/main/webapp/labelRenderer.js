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