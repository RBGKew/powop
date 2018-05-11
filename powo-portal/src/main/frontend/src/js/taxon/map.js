define(['jquery', 'libs/lodash', 'libs/openlayers'], function($, _, ol) {
  var projection = 'EPSG:3857';
  var taxonId = $('.s-page').attr('id');
  var serverResolutions = [
    156543.0339,
    78271.51695,
    39135.758475,
    19567.8792375,
    9783.93961875,
    4891.969809375,
    2445.9849046875
  ];

  var baseLayer = new ol.layer.Tile({
    title: 'Base Layer',
    projection: projection,
    source: new ol.source.XYZ({
      url: 'https://storage.googleapis.com/powop-assets/tiles/{z}/{x}/{-y}.png'
    })
  });

  var map = new ol.Map({
    target: 'c-map',
    layers: [ baseLayer ],
    logo: false,
    interactions: ol.interaction.defaults({mouseWheelZoom:false}),
    view: new ol.View({
      projection: projection,
      maxResolution: serverResolutions[2],
      minResolution: serverResolutions[4],
    })
  });

  function featureIds(distributions, establishment) {
    return _.chain(distributions)
      .filter({'establishment': establishment})
      .map('featureId')
      .value()
      .join();
  }

  function distinctLayers(distributions) {
    return _.chain(distributions)
      .filter(function(d) {return d.establishment !== 'Absent'})
      .map(function(d) {return {level: d.tdwgLevel, establishment: d.establishment}})
      .uniqWith(_.isEqual)
      .value();
  }

  function addLegend(overlays) {
    var acceptable = ['Doubtful', 'Native', 'Extinct', 'Introduced'];
    var legend = '<div class="legend"><p>'
    _.forEach(acceptable, function(layer) {
      if(overlays[layer]) {
        legend += '<span class="' + layer + '"></span>' + layer;
      }
    });
    legend += '</p></div>';

    $('#c-map').after(legend);
  }

  function addLayer(distributions, layer) {
    sourceParams = {
      url: '/geoserver/wms',
      params: {
        LAYERS: 'tdwg:level' + layer.level,
        STYLES: 'tdwg_level' + layer.level + '_' + layer.establishment.toLowerCase(),
        FEATUREID: featureIds(distributions, layer.establishment),
      }
    };

    map.addLayer(new ol.layer.Tile({
      source: new ol.source.TileWMS(sourceParams)
    }));

    return layer.establishment;
  }

  function fitTo(envelope) {
    bounds = new ol.geom.MultiPoint(
      _.map(envelope, function(coordinate) {
        return [coordinate.x, coordinate.y];
      })
    );

    map.getView().fit(bounds, map.getSize());
  }

  function loadDistributionLayers() {
    $.getJSON('/api/1/taxon/' + taxonId, function(taxon) {
      var legend = {};

      _.forEach(distinctLayers(taxon.distributions), function(layer) {
        legend[addLayer(taxon.distributions, layer)] = true;
      });

      fitTo(taxon.distributionEnvelope);
      addLegend(legend);
    });
  }

  var initialize = function() {
    loadDistributionLayers();
  }

  return {
    initialize: initialize,
    map: map
  }
});
