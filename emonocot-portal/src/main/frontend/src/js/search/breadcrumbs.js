define(['jquery', 'templates/partials/search/filter-breadcrumb.js'], function($, tmpl) {
  var breadcrumbs = {};

  var add = function(key, value) {
    if(breadcrumbs[key]) {
      remove(key);
    }

    breadcrumbs[key] = value;
    $('.c-search__filters .btn-group').append(tmpl({searchTerm: key, searchValue: value}));
  };

  var remove = function(key) {
    delete breadcrumbs[key];
    $('.c-search__filters button.' + key).remove();
  };

  var toString = function() {
    return $.param(breadcrumbs);
  };

  return {
    add: add,
    remove: remove,
    toString: toString,
  };
});
