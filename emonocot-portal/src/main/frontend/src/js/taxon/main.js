define(['jquery', './map', 'bootstrap', 'libs/magnific-popup'], function($, map) {
  var initialize = function() {
    // initialize popup for header image
    $('.c-gallery-header a').magnificPopup({type: 'image'});

    // initialize popups for image gallery
    $('.c-gallery').magnificPopup({
      delegate: 'a',
      type: 'image',
      gallery: { enabled: true }
    });

    // Accomodate fixed header when jumping to anchor links
    $('a[href*=#]:not([href=#])').click(function() {
      if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') || location.hostname == this.hostname) {

        var target = $(this.hash);
        var headerHeight = $(".c-article-nav").height(); // Get fixed header height

        target = target.length ? target : $('[name=' + this.hash.slice(1) +']');

        if (target.length) {
          $('html,body').animate({
            scrollTop: target.offset().top - headerHeight
          }, 'fast', 'swing');
          return false;
        }
      }
    })

    $('body').scrollspy({
      target: '.navbar',
      offset: 75
    });

    map.initialize();
  }

  return {
    initialize: initialize,
  }
});
