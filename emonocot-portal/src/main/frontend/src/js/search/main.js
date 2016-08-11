define(['./breadcrumbs'], function(breadcrumbs) {
  return {
    add: function(key, value) { breadcrumbs.add(key, value); },
    search: function() { return breadcrumbs.toString(); }
  };
});
