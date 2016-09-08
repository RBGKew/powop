define(function(require) {
  var $ = require('jquery');
  var lightbox = require('lightbox');

  var initialize = function() {
    $(document).ready(function() {
      $(document).on('click', '[data-toggle="lightbox"]', function(e) {
        e.preventDefault();
        $(this).ekkoLightbox();
      });
    });
  }

  return {
    initialize: initialize,
  }
});
