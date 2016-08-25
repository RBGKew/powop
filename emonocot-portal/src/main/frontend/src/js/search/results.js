define(['jquery', 'templates/partials/result/results.js', ], function($, tmpl) {

  var update = function(state) {

    $.getJSON("/api/1/search?" + state)
    .done(function( json ) {
      $('.c-results').replaceWith(tmpl(json));
  });
  };

  return {
    update : update,
  };
});
