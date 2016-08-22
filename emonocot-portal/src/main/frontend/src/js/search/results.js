define(['jquery', 'templates/partials/result/results.js', ], function($, tmpl) {

  var update = function(breadcrumbs) {
    $.getJSON("/api/1/search?" + breadcrumbs)
    .done(function( json ) {
      $('.c-results').replaceWith(tmpl(json));
  });
  };

  return {
    update : update,
  };
});
