# Custom Build

We use a custom build of openlayers since the full library is quite large. The build
descriptor is ol-custom.json. If new features are used, they must be added to the
"exports" section of ol-custom.json.

To build the custom library, run

```
node node_modules/openlayers/tasks/build.js ol-custom.json src/js/libs/openlayers.js
```
