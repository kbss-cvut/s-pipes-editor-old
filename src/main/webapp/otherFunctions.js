function makeItBeautiful(){
      
      var config = {
        rankdir: 'TB'
        };

      // configure the algorithm:
      var listener = sigma.layouts.dagre.configure(s, config);

      // Bind all events:
      listener.bind('start stop interpolate', function(event) {
        console.log(event.type);
      });

      listener = sigma.layouts.dagre.start(s);
      s.refresh();
    };

    
    function exportJSON(){
      var nodes1 = JSON.parse(JSON.stringify(s.graph.nodes()));
      var edges1 = JSON.parse(JSON.stringify(s.graph.edges()));
      nodes1[0].coordinates = undefined;
      for (var i = 0; i < nodes1.length; i++){
        nodes1[i].outParamsZones = undefined;
        nodes1[i].url = undefined;
        nodes1[i].x = undefined;
        nodes1[i].y = undefined;
        nodes1[i].coordinates = undefined;
        nodes1[i].forceLabel = undefined;
        nodes1[i].inParamsZones = undefined;
        nodes1[i]['read_cam0:size'] = undefined;
        nodes1[i]['read_cam0:x'] = undefined;
        nodes1[i]['read_cam0:y'] = undefined;
        nodes1[i]['renderer1:size'] = undefined;
        nodes1[i]['renderer1:x'] = undefined;
        nodes1[i]['renderer1:y'] = undefined;
        nodes1[i].size = undefined;
      }
      for (var i = 0; i < edges1.length; i++){
        edges1[i].coordinates = undefined;
        edges1[i].forceLabel = undefined;
        edges1[i].inParamsZones = undefined;
        edges1[i]['read_cam0:size'] = undefined;
        edges1[i]['renderer1:size'] = undefined;
        edges1[i].size = undefined;
      }
      console.log(nodes1);
      var a = JSON.stringify({nodes: nodes1, edges: edges1}, null, '\t'); 
      download(a, 'graph.json', 'application/json');
      console.log(a);
    }

    
    function loadJSON(callback){
      var xobj = new XMLHttpRequest();
        xobj.overrideMimeType("application/json");
      xobj.open('GET', 'graph.json', true); // Replace 'my_data' with the path to your file
      xobj.onreadystatechange = function () {
          if (xobj.readyState == 4 && xobj.status == "200") {
            // Required use of an anonymous callback as .open will NOT return a value but simply returns undefined in asynchronous mode
            callback(xobj.responseText);
          }
    };
    xobj.send(null); 
    }

    function importJSON(){
      loadJSON(function(response) {
        clearAll();
        var actual_JSON = JSON.parse(response);
        console.log("json", actual_JSON);
        for (var i in actual_JSON.nodes)
        {
          s.graph.addNode ({
          // Main attributes:
          id: actual_JSON.nodes[i].id,
          label: actual_JSON.nodes[i].label,
          x: 0 + actual_JSON.nodes[i].id/5,
          y: 0 + actual_JSON.nodes[i].id/5,
          coordinates: [],
          type: actual_JSON.nodes[i].type,
          size: 15,
          color: typeCharacteristics[actual_JSON.nodes[i].type][0],
          url: typeCharacteristics[actual_JSON.nodes[i].type][1],
          inParams: actual_JSON.nodes[i].inParams,
          inParamsZones: [],
          outParams: actual_JSON.nodes[i].outParams,
          outParamsZones: [],
          });
          idN = actual_JSON.nodes[i].id;
          s.refresh();
        }
        for (var i in actual_JSON.edges)
        {
          s.graph.addEdge({
            id: actual_JSON.edges[i].id,
            source: actual_JSON.edges[i].source,
            target: actual_JSON.edges[i].target,
            sourceZone: actual_JSON.edges[i].sourceZone,
            targetZone: actual_JSON.edges[i].targetZone
          });
          idE = actual_JSON.edges[i].id;
        }
        makeItBeautiful();
        s.refresh();
      }); 
    }