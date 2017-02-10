sigma.parsers.json
==================

Plugin developed by [Alexis Jacomy](https://github.com/jacomyal).

---

This plugin provides a single function, `sigma.parsers.json()`, that will load a JSON encoded file, parse it, eventually instantiate sigma and fill the view with the `view.read()` method. The main goal is to avoid using jQuery only to load an external JSON file.

The most basic way to use this helper is to call it with a path and a sigma configuration object. It will then instanciate sigma, but after having added the view into the config object.

````javascript
sigma.parsers.json(
  'myGraph.json',
  { container: 'myContainer' }
);
````

It is also possible to update an existing instance's view instead.

````javascript
sigma.parsers.json(
  'myGraph.json',
  myExistingInstance,
  function() {
    myExistingInstance.refresh();
  }
);
````
