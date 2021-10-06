define(function(require) {

  var $ = require('jquery');
  var descriptions = require('./descriptions')
  var bibliography = require('./bibliography')
  var map = require('./map');
  var filters = require('../search/filters');
  require('libs/bootstrap');
  require('libs/magnific-popup');


  var initialize = function() {

  // setup search box
  filters.initialize();
  filters.tokenfield().on('tokenfield:createtoken', function(e) {
    e.preventDefault();
    window.location = '../results?q=' + e.attrs.value;
  });

  $(document).on('click', '#search-button', function(e) {
    e.preventDefault();
    if ($('.token-input').val()) {
      window.location = '../results?q=' + $('.token-input').val();
    }
  })

  $('.pagination .disabled a, .pagination .active a').on('click', function(e) {
    e.preventDefault();
  });

  initToTopBehaviour();

  function initToTopBehaviour() {
    $(window).scroll(function() {
      if ($(this).scrollTop() >= $('.navbar--article').position().top) {
        $(".to-top").css("display", "block");
      } else {
        $(".to-top").css("display", "none");
      }
    });
    $('.to-top').on('click', function(e) {
      $('html,body').animate({ scrollTop: 0 }, 'fast', 'swing');
    });
  }

  // this targets the Map on the taxon page that is created with open layers
  $('.ol-unselectable').attr('aria-label', 'Distribution Map');

  $('.navbar--article').on('click', function(e) {
    e.preventDefault();
	$('body').css('overflow', 'auto');
  });
    
    
  $('.tokenfield input')
    .on('focus', function() {
      $('#search_box')
        .addClass('focused');
    })
    .on('blur', function() {
      $('#search_box')
        .removeClass('focused');
    })

    // initialize popup for header image
    $('.c-gallery-header').on('click', function(e) {
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
    $('nav a').on('click', function() {
      var target = $(this.hash);
      var headerHeight = $(".c-article-nav").height(); // Get fixed header height

      if (target.length) {
        $('html,body').animate({
          scrollTop: target.offset().top - headerHeight + 5
        }, 'fast', 'swing');
      }
    });
    navToggle();

    function navToggle() {
        // opens taxon nav when return  key is pressed
      $(".mobile-menu").keypress(function (e) {
          if (e.keyCode === 13) {
            $('.navbar-collapse').collapse('toggle')
          }
      });
      
      // hides taxon nav when clicked
      $('.navbar-nav>li>a').on('click', function(){
        $('.navbar-collapse').collapse('hide');
      });
    }

    // enable scrollspy on navbar
    $('body').scrollspy({
      target: '.navbar',
      offset: 75
    });

    // enable popovers
    $('[data-toggle="popover"]').popover();

    // open collapsed section if loading with location hash
    if(location.hash.length > 0) {
      $(location.hash + ' .container').addClass('in');
    }

    bibliography.initialize()

    if($('#c-map').length) {
      map.initialize();
    }
  }

  return {
    initialize: initialize,
  }
});
