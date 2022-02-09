define(function(require) {

  var $ = require('jquery');
  var descriptions = require('./descriptions')
  var bibliography = require('./bibliography')
  var gallery = require('./gallery')
  var map = require('./map');
  var search = require('search');
  require('libs/bootstrap');
  require('libs/magnific-popup');


  var initialize = function() {

  search.initRedirectSearch();

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
  $('.c-map canvas.ol-unselectable').attr('aria-label', 'Distribution Map');

    gallery.initialize();

    initInternalPageNavigationBehaviour();

    function initInternalPageNavigationBehaviour() {
      // opens taxon nav when return key is pressed
      $(".mobile-menu").on("keypress", function (e) {
        if (e.key === 13) {
          $('.navbar-collapse').collapse('toggle')
        }
      });
      
      // hides taxon nav when clicked
      $('.navbar-nav>li>a').on('click', function () {
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

    descriptions.initialize()
    bibliography.initialize()

    if($('#c-map').length) {
      map.initialize();
    }
  }

  return {
    initialize: initialize,
  }
});
