/**
 * State module: Keeps track of the state of applied search filters
 *
 * Publishes events on 'search.filters' channel
 */
define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Immutable = require('libs/immutable');
  var pubsub = require('libs/pubsub');
  var deparam = require('libs/deparam');
  var Bloodhound = require('libs/bloodhound');
  var typeahead = require('libs/typeahead');
  var Handlebars = require('handlebars');
  var suggestionTmpl = require('templates/partials/search/suggestion.js');

  var initialized = false;
  var tokenfield;
  var params = Immutable.Map();

  var suggesters = [
    'common-name',
    'scientific-name',
    'location',
    'characteristic',
  ];

  function humanize(name) {
    return name.split('-').join(' ');
  }

  function transform(res) {
    ret = [];
    _.each(suggesters, function(suggester) {
      if(!(suggester in res.suggestedTerms)) {
        return;
      }

      for(i = 0; i < res.suggestedTerms[suggester].length && i < 2; i++) {
        ret.push({
          value: res.suggestedTerms[suggester][i],
          category: humanize(suggester),
        });
      }
    });
    return ret;
  }

  var initialize = function(initial) {
    engine = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.whitespace,
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '/api/1/suggest?query=%q',
        wildcard: '%q',
        transform: transform,
      }
    });
    engine.initialize();

    tokenfield = $('input.refine').tokenfield({
      tokens: initial,
      typeahead: [
        {
          hint: false,
          highlight: true,
        },
        {
          display: 'value',
          limit: 8,
          source: engine.ttAdapter(),
          templates: { suggestion: suggestionTmpl }
        }
      ]
    })
      .on('tokenfield:createtoken', customizeTokenLabels)
      .on('tokenfield:createdtoken', createdToken)
      .on('tokenfield:removedtoken', publishUpdated);

    initialized = true;
  }

  var overrides = {'source:Kew-Species-Profiles': 'Kew Species Profiles'};
  function customizeTokenLabels(e) {
    if(e.attrs.label in overrides) {
      e.attrs.label = overrides[e.attrs.label];
    }
  }

  function publishUpdated(e) {
    pubsub.publish('search.updated');
  };

  function createdToken() {
    publishUpdated();
  };

  function setTokens(tokens) {
    if(initialized) {
      tokenfield.tokenfield('setTokens', tokens);
    } else {
      initialize(tokens);
      publishUpdated();
    }
  }

  var add = function(key, val) {
    tokenfield.tokenfield('createToken', val ? key + ":" + val : key);
  }

  var getFilters = function() {
    return tokenfield.tokenfield('getTokens');
  }

  var setParam = function(key, value, publish) {
    params = params.set(key, value);
    if(_.defaultTo(publish, true)) {
      pubsub.publish('search.updated.params.' + key);
    }
  };

  var getParam = function(key) {
    return params.get(key);
  };

  var removeParam = function(key, publish) {
    params = params.delete(key);
    if(_.defaultTo(publish, true)) {
      pubsub.publish('search.updated.params.' + key);
    }
  }

  var serialize = function() {
    var q = params.toObject();
    if(!_.isEmpty(this.filters())) {
      $.extend(q, {'q': _.map(this.filters(), 'value').join(',')});
    }

    return($.param(q));
  };

  var deserialize = function(serialized) {
    if(serialized[0] == '?') {
      serialized = serialized.slice(1);
    }

    var deserialized = deparam(serialized);
    for(key in deserialized) {
      if(key === 'q') continue;
      params = params.set(key, deserialized[key]);
    }

    if(_.isString(deserialized['q'])) {
      setTokens(deserialized['q'].split(','));
    } else {
      publishUpdated();
    }
  }

  var refresh = function() {
    tokenfield.tokenfield('update');
  }

  return {
    add: add,
    deserialize: deserialize,
    filters: getFilters,
    getParam: getParam,
    initialize: initialize,
    refresh: refresh,
    removeParam: removeParam,
    serialize: serialize,
    setParam: setParam,
  }
});
