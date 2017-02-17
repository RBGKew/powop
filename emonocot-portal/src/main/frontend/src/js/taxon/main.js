define(['jquery', './map', 'bootstrap', 'libs/magnific-popup'], function($, map) {
  var initialize = function() {

    // collapse all sections if screen size is less than 768px
    if ($(window).width() < 768) {
      $(".c-article-section").map(function(__, section) {
        $(section).find('.container').removeClass("in");
        $(section).find('a.collapser').first().addClass('collapsed');
      });
    }

    // initialize popup for header image
    $('.c-gallery-header').click(function(e) {
      $('.c-gallery').magnificPopup('open');
      e.preventDefault();
    });

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
            scrollTop: target.offset().top - headerHeight + 5
          }, 'fast', 'swing');
          return false;
        }
      }
    })

    // enable scrollspy on navbar
    $('body').scrollspy({
      target: '.navbar',
      offset: 75
    });

    // open collapsed sections when navbar link clicked
    $('.c-article-nav a').click(function() {
      $($(this).attr('href') + ' .container').collapse('show');
    });

    // enable popovers
    $('[data-toggle="popover"]').popover();

    if($('#c-map').length) {
      map.initialize();
    }
  }

  return {
    initialize: initialize,
  }
});
