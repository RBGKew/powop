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
  var tokenfieldAccessibility = require("./tokenfield-accessibility");
  var suggestionTmpl = require('templates/partials/search/suggestion.js');

  var tokenfield;
  var params = Immutable.Map();

  var suggesters = [
    'common-name',
    'scientific-name',
    'location',
    'characteristic',
  ];

  function suggesterToGlyphicon(suggester) {
    switch(suggester) {
      case 'common-name':
        return 'common name';
      case 'scientific-name':
        return 'scientific name';
      case 'location':
        return 'location';
      case 'characteristic':
        return 'characteristic';
    }
  }

  function transform(res) {
    ret = [];
    var totalSuggestions = _.reduce(
      res.suggestedTerms,
      function(n, sug) { return n + sug.length },
      0
    );

    _.each(suggesters, function(suggester) {
      if(!(suggester in res.suggestedTerms)) {
        return;
      }

      for(i = 0; i < res.suggestedTerms[suggester].length && i < 5; i++) {
        var val = res.suggestedTerms[suggester][i];
        ret.push({
          value: (suggester === suggesters[0] || suggester == suggesters[1]) ? val : suggester + ":" + val,
          display: val,
          category: suggesterToGlyphicon(suggester),
        });
      }
    });

    return ret;
  }


  var initialize = function() {
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
      allowPasting: false,
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
      .on('tokenfield:createdtoken', tokenChanged)
      .on('tokenfield:removedtoken', tokenChanged);

    tokenfieldAccessibility.updateCopyHelper(tokenfield);
    $(window).on('resize', refresh);
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

  function tokenChanged() {
    tokenfieldAccessibility.updateTokenRemoveButtons(tokenfield);
    params = params.clear();
    publishUpdated();
  };

  var add = function(key, val) {
    tokenfield.tokenfield('createToken', val ? key + ":" + val : key);
  }

  var getFilters = function() {
    if (tokenfield) {
      return tokenfield.tokenfield('getTokens');
    }
  }

  var getParam = function(key) {
    return params.get(key);
  };

  var toggleFacet = function(facet) {
    var facets = params.get('f');
    facets = _.isUndefined(facets) ? [] : _.split(facets, ',');
    if(_.includes(facets, facet)) {
      _.remove(facets, function(f) { return f === facet });
    } else {
      facets.push(facet);
    }

    params = params.delete('cursor');
    params = params.delete('p');
    params = params.set('f', _.join(facets, ','));
    publishUpdated();
  }

  var setSort = function(sort) {
    params = params.set('sort', sort);
    publishUpdated();
  }

  var setCursor = function(cursor, page) {
    params = params.set('cursor', cursor);
    params = params.set('p', page);
    publishUpdated();
  }

  var setPageSize = function(pageSize) {
    params = params.set('page.size', pageSize);
    publishUpdated();
  }

  var serialize = function(override) {
    var q = params.toObject();

    if(!_.isEmpty(override)) {
      _.merge(q, override);
    }

    if(!_.isEmpty(this.filters())) {
      $.extend(q, {'q': _.map(this.filters(), 'value').join(',')});
    }

    return($.param(q));
  };

  var deserialize = function(serialized, publish) {
    if(serialized[0] == '?') {
      serialized = serialized.slice(1);
    } else {
      return;
    }

    var deserialized = deparam(serialized);
    for(key in deserialized) {
      if(key === 'q') {
        tokenfield.tokenfield('setTokens', deserialized['q'].split(','), false, publish);
      } else {
        params = params.set(key, deserialized[key]);
      }
    }

    if(_.defaultTo(publish, true)) {
      publishUpdated();
    }
  }

  var refresh = function() {
    if (tokenfield) {
      tokenfield.tokenfield('update');
    }
  }

  var getTokenfield = function() {
    return tokenfield;
  }

  return {
    add: add,
    deserialize: deserialize,
    filters: getFilters,
    getParam: getParam,
    initialize: initialize,
    refresh: refresh,
    serialize: serialize,
    toggleFacet: toggleFacet,
    setSort: setSort,
    setCursor: setCursor,
    setPageSize: setPageSize,
    tokenfield: getTokenfield,
  }
});
