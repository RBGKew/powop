# OpenLayers Custom Build

We use a custom build of openlayers since the full library is quite large. The build descriptor is ol-custom.json.
If new features are used we will need to upgrade to `ol` rather than `openlayers` as the old version is no longer
supported, and the new version does not have the same build setup for generating a library (but is compatible with
Webpack / Rollup instead)
