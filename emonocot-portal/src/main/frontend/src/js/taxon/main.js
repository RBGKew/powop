define(function(require) {
  var $ = require('jquery');
  var lightbox = require('lightbox');

  $(document).ready(function() {
    $(document).on('click', '[data-toggle="lightbox"]', function(e) {
      e.preventDefault();
      $(this).ekkoLightbox();
    });
  });
});
