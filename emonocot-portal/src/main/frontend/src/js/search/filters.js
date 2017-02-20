/**
 * State module: Keeps track of the state of applied search filters
 *
 * Publishes events on 'search.filters' channel
 */
define([
  'jquery',
  'immutable',
  'libs/pubsub',
  'libs/deparam',
  'templates/partials/search/filter-breadcrumb.js',
], function($, Immutable, pubsub, deparam, tmpl) {

  var filters = Immutable.Map();

  var params = Immutable.Map();

  function className(key) {
    return key.toLowerCase().replace(' ', '-');
  }

  function addBreadcrumb(key, value) {
    var bar = $('.c-search__filters');
    if(bar.is(":hidden")) {
      bar.show();
    }
    if(key == "source" && value == "Kew-Species-Profiles"){
      $(bar.find('.btn-group')).append(tmpl({
        searchTitle: "Kew Species Profile",
        searchTerm: key,
        searchValue: value,
        className: className(key)
      }));
    }else{
      $(bar.find('.btn-group')).append(tmpl({
        searchTerm: key,
        searchValue: value,
        className: className(key)
      }));
    }
  }

  function doAdd(key, value) {
    if(filters.has(key)) {
      doRemove($('button.' + className(key)));
    }
    if($.isArray(value)) {
      filters = filters.set(key, value);
      $.each(value, function(index, val) { addBreadcrumb(key, val) });
    } else {
      filters = filters.set(key, [value]);
      addBreadcrumb(key, value)
    }

    params = params.remove('page.number');
  }

  function doRemove(filter) {
    var key = $(filter).data('term');
    var value = $(filter).data('value');

    if($.isArray(filters.get(key)) && filters.get(key).length > 1) {
      var updated = $.grep(filters.get(key), function(v) {
        return value != v;
      });
      filters = filters.set(key, updated);
    } else {
      filters = filters.delete(key);
    }

    params = params.remove('page.number');
    filter.remove();
    return key;
  }

  var add = function(key, value) {
    doAdd(key, value);
    console.log('Added {' + key + ', ' + value + '}. Current Filters: ' + filters.toString());
    pubsub.publish('search.updated.filters.' + key, true);
  };

  var remove = function(removeFilters) {
    var key;
    if(removeFilters.length > 1) {
      removeFilters.each(function(_, filter) {
        key = doRemove(filter);
      });
    } else {
       key = doRemove(removeFilters);
    }

    if(filters.isEmpty()) {
      $('.c-search__filters').hide();
    }

    console.log('Removed {' + key + '}. Current Filters: ' + filters.toString());
    pubsub.publish('search.updated.filters.' + key, true);
  };

  var toQuery = function() {
    var queryMap = {};
    var flatFilterMap = {};
    var filterMap = filters.toObject();
    var paramMap = params.toObject();
    for(var key in filterMap){
      queryMap[key] = filterMap[key].join(" AND ");
    }
    $.extend(queryMap, paramMap)
    console.log($.param(queryMap));
    return($.param(queryMap));
  };

  var setParam = function(key, value) {
    params = params.set(key, value);
    pubsub.publish('search.updated.params.' + key, true);
  };

  var getParam = function(key) {
    return params.get(key);
  };

  var serialize = function() {
    return $.param({
      filters: filters.toObject(),
      params: params.toObject()
    });
  };

  var deserialize = function(querystr) {
    if(querystr[0] === '?') {
      querystr = querystr.slice(1);
    }

    var deserialized = deparam(querystr);
    for(key in deserialized.filters) {
      doAdd(key, deserialized.filters[key]);
    }

    for(key in deserialized.params) {
      params = params.set(key, deserialized.params[key]);
    }
    pubsub.publish('search.updated', false);
  }
  var removeParam = function(key) {
    params.delete(key);
    pubsub.publish('search.updated.params.' + key, true);

  }

  return {
    add: add,
    set: add,
    setParam: setParam,
    getParam: getParam,
    removeParam: removeParam,
    remove: remove,
    toQuery: toQuery,
    serialize: serialize,
    deserialize: deserialize
  };
});
