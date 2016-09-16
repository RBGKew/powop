define(['jquery', './map', 'libs/magnific-popup'], function($, map) {
  var initialize = function() {
    // initialize popup for header image
    $('.c-gallery-header a').magnificPopup({type: 'image'});

    // initialize popups for image gallery
    $('.c-gallery').magnificPopup({
      delegate: 'a',
      type: 'image',
      gallery: { enabled: true }
    });

    $('.description_expand').click(function( event ){
      event.preventDefault();
      $(this).find("span")
        .toggleClass('glyphicon-chevron-up')
        .toggleClass('glyphicon-chevron-down');
      $(this).parent().next().toggle();
    });

    map.initialize();
  }

  return {
    initialize: initialize,
  }
});
