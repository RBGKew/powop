define(['jquery', 'libs/magnific-popup'], function($) {
  var initialize = function() {
    // initialize popup for header image
    $('.c-gallery-header a').magnificPopup({type: 'image'});

    // initialize popups for image gallery
    $('.c-gallery').magnificPopup({
      delegate: 'a',
      type: 'image',
      gallery: { enabled: true }
    });
  }

  return {
    initialize: initialize,
  }
});
