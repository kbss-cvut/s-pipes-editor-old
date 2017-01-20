var idN = 0,
    idE = 0,
    g = {
        nodes: [],
        edges: []
    };


// creating an instance of sigma
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
        labelThreshold: 70,
        //sideMargin: 0.5,
    }
});