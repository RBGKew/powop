/**
 * Filters module: Keeps track of the state of applied search filters
 *
 * Publishes events on 'search.filters' channel
 */
define([
  'jquery',
  'libs/pubsub',
  'templates/partials/search/filter-breadcrumb.js',
  'immutable',
  ], function($, pubsub, tmpl, Immutable) {

  var filters = Immutable.Map();

  var add = function(key, value) {
    if(filters.has(key)) {
      doRemove($('button.' + key));
    }

    if($.isArray(value)) {
      filters = filters.set(key, value);
      $.each(value, function(index, val) { addBreadcrumb(key, val) });
    } else {
      filters = filters.set(key, [value]);
      addBreadcrumb(key, value)
    }

    pubsub.publish('search.filters.' + key, filters.get(key));
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

    pubsub.publish('search.filters.' + key, filters.get(key));
  };

  var toString = function() {
    var paramMap = {};
    var filterMap = filters.toObject();
    for(key in filterMap){
      paramMap[key] = filterMap[key].join(" AND ");
    }
     return($.param(paramMap));
  };


  function addBreadcrumb(key, value) {
    $('.c-search__filters .btn-group').append(tmpl({searchTerm: key, searchValue: value}));
  }

  function doRemove(filter) {
    var key = $(filter).data('term');
    var value = $(filter).data('value');

    if($.isArray(filters.get(key))) {
      var updated = $.grep(filters.get(key), function(v) {
        return value != v;
      });

      filters = filters.set(key, updated);
    } else {
      filters = filters.delete(key);
    }

    filter.remove();
    return key;
  }

  return {
    add: add,
    set: add,
    remove: remove,
    toString: toString,
  };
});
