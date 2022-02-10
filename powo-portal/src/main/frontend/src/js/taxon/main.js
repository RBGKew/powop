define(function(require) {

  var $ = require('jquery');
  var descriptions = require('./descriptions')
  var bibliography = require('./bibliography')
  var gallery = require('./gallery')
  var map = require('./map');
  var filters = require('../search/filters');
  var focus = require("../common/focus");
  require('libs/bootstrap');
  require('libs/magnific-popup');

  function initialize() {

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

  function setToTopVisibility() {
    var scrollTop = $(window).scrollTop()
    var navbarTop = $(".navbar--article").position().top
    var visible = scrollTop >= navbarTop;
    $(".to-top")
      .css("display", visible ? "block" : "none")
      .attr("aria-hidden", !visible);
  }

  function initToTopBehaviour() {
    setToTopVisibility();

    $(window).on("scroll", function () {
      setToTopVisibility();
    });

    $(".to-top").on("click", function (e) {
      $("html, body").animate({ scrollTop: 0 }, "fast", "swing");
    });
  }

  // this targets the Map on the taxon page that is created with open layers
  $('.c-map canvas.ol-unselectable').attr('aria-label', 'Distribution Map');

  $('.tokenfield input')
    .on('focus', function() {
      $('#search_box')
        .addClass('focused');
    })
    .on('blur', function() {
      $('#search_box')
        .removeClass('focused');
    })

    gallery.initialize();

    initInternalPageNavigationBehaviour();

    function initInternalPageNavigationBehaviour() {
      var releaseFocus;

      $("#navbarSupportedContent").on("show.bs.collapse", function () {
        console.log("show.bs.collapse");
        var navbar = $(".navbar--article")[0];
        releaseFocus = focus.containFocus(navbar);
      });

      $("#navbarSupportedContent").on("hide.bs.collapse", function () {
        console.log("hide.bs.collapse");
        if (releaseFocus) {
          releaseFocus();
        }
      });

      // hides taxon nav when clicked
      $(".navbar-nav > li > a").on("click", function () {
        $(".navbar-collapse").collapse("hide");
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
