define(function(require) {

  var $ = require('jquery');
  var map = require('./map');
  var History = require('libs/native.history');
  require('libs/bootstrap');
  require('libs/magnific-popup');

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
    $('nav a').click(function() {
      var target = $(this.hash);
      var headerHeight = $(".c-article-nav").height(); // Get fixed header height

      if (target.length) {
        $('html,body').animate({
          scrollTop: target.offset().top - headerHeight + 5
        }, 'fast', 'swing');
      }
    });

    // enable scrollspy on navbar
    $('body').scrollspy({
      target: '.navbar',
      offset: 75
    });

    // open collapsed sections when navbar link clicked
    $('.c-article-nav a').click(function() {
      $($(this).attr('href') + ' .container').collapse('show');
    });

    // open collapsed section if loading with location hash
    if(location.hash.length > 0) {
      $(location.hash + ' .container').addClass('in');
    }

    // enable tooltips
    $('.description a[title]').tooltip();

    if($('#c-map').length) {
      map.initialize();
    }
  }

  return {
    initialize: initialize,
  }
});
