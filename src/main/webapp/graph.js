
// function for downloading graph as json file
function download(strData, strFileName, strMimeType) {
    var D = document,
        A = arguments,
        a = D.createElement("a"),
        d = A[0],
        n = A[1],
        t = A[2] || "application/json";

    //build download link:
    a.href = "data:" + strMimeType + "charset=utf-8," + escape(strData);

    if (window.MSBlobBuilder) { // IE10
        var bb = new MSBlobBuilder();
        bb.append(strData);
        return navigator.msSaveBlob(bb, strFileName);
    } /* end if(window.MSBlobBuilder) */

    if ('download' in a) { //FF20, CH19
        a.setAttribute("download", n);
        a.innerHTML = "downloading...";
        D.body.appendChild(a);
        setTimeout(function() {
            var e = D.createEvent("MouseEvents");
            e.initMouseEvent("click", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            a.dispatchEvent(e);
            D.body.removeChild(a);
        }, 66);
        return true;
    }; /* end if('download' in a) */

    //do iframe dataURL download: (older W3)
    var f = D.createElement("iframe");
    D.body.appendChild(f);
    f.src = "data:" + (A[2] ? A[2] : "application/octet-stream") + (window.btoa ? ";base64" : "") + "," + (window.btoa ? window.btoa : escape)(strData);
    setTimeout(function() {
        D.body.removeChild(f);
    }, 333);
    return true;
}
// clear the whole canvas
function clearAll()
{
  for (i = s.graph.nodes().length-1; i != - 1; i--)
    s.graph.dropNode(i);
  idN = 0;
  idE = 0;
  
  s.refresh();
  console.log("cleared", s);
}



// chrarcteristics for every type of node: ['color', 'name of img']
var typeCharacteristics = [
      [ '#f5ceca', 'img1'],
      [ '#dfcde0', 'img2'],
      [ '#e5f98d', 'img3']
    ];



sigma.utils.pkg('sigma.canvas.nodes');
sigma.utils.pkg('sigma.canvas.edges');
sigma.utils.pkg('sigma.canvas.labels');


// creating an instance of sigma   
/*s = new sigma({
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
     labelThreshold: 70,
     //sideMargin: 0.5,
  }
});*/






function newNode(typeOfNode)
{
  switch (typeOfNode)
  {
    case 1:
    s.graph.addNode ({
      // Main attributes:
      id: idN,
      label: 'ahjbfgvjhgfvbhbvjf' + idN,
      x: 0 + idN/5,
      y: 0 + idN/5,
      coordinates: [],
      type: 0,
      size: 15,
      color: '#f5ceca',
      //url: 'img1',
      forceLabel: true,
      inParams: [],
      inParamsZones: [],
      outParams: ['o1', 'o22', 'o333', 'o4'],
      outParamsZones: [],
    }); 
    var arr = [];

        break;

    case 2:
    s.graph.addNode ({
      // Main attributes:
      id: idN,
      label: 'nodeeee' + idN,
      x: 0 + idN/5,
      y: 0 + idN/5,
      coordinates: [],
      type: 1,
      size: 15,
      color: '#dfcde0',
      //url: 'img2',
        inParams: [],
      inParamsZones: [],
      outParams: [],
      outParamsZones: []
    });
        break;

    case 3:
    s.graph.addNode ({
      // Main attributes:
      id: idN,
      label: 'n' + idN,
      x: 0 + idN/5,
      y: 0 + idN/5,
      coordinates: [],
      type: 2,
      size: 15,
      color: '#e5f98d',
      //url: 'img3',
      inParams: [],
      inParamsZones: [],
      outParams: ['out1'],
      outParamsZones: [],
    });
    break
  }
  console.log("Added ", s.graph.nodes()[idN]);
    console.log("Nodes ", s.graph.nodes());
  idN++; 
  s.refresh();





  
  // this block is needed because of canvas bug - text in 
  // 'inParams' and 'outParams' will behave is strange way without this code
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


    

//get the popup box
var modal = document.getElementById('myModal');


var dragListener = sigma.plugins.dragNodes(s, s.renderers[0]);
dragListener.bind('drop', function(event) {
  console.log('Now: x ', s.graph.nodes()[0]);
  drawingEdge = false;
});

// events    

    
CustomShapes.init(s);
s.refresh();