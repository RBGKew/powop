define(['jquery'], function($, tmpl) {
  var breadcrumbs = {};

  return {
    add: function(key, value) {
      breadcrumbs[key] = value;
    },

    remove: function(key) {
      delete breadcrumbs[key];
    },

    toString: function() {
      return $.param(breadcrumbs);
    }
  };
});
