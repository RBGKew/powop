define(function(require) {

  var $ = require('jquery');
  var map = require('./map');
  var History = require('libs/native.history');
  require('libs/bootstrap');
  require('libs/magnific-popup');

  var bibliographyTmpl = require('templates/partials/taxon/bibliography.js');

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

    if($('#content-navbar').length > 0) {
      $('#content-navbar').affix({
          offset: { top: $('#content-navbar').offset().top }
      });
    }


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

    // enable popovers
    $('[data-toggle="popover"]').popover();

    // open collapsed section if loading with location hash
    if(location.hash.length > 0) {
      $(location.hash + ' .container').addClass('in');
    }

    // enable tooltips
    $('.description a[title]').tooltip();

    // bibliography sorting
    $('#sort-bibliography-by-citation').click(function(e) {
      sortBibliography(e, ['bibliographicCitation'], ['asc']);
      $(this).addClass('selected_background');
    });
    $('#sort-bibliography-by-newest-first').click(function(e) {
      sortBibliography(e, ['date', 'bibliographicCitation'], ['desc', 'asc']);
      $(this).addClass('selected_background');
    });
    $('#sort-bibliography-by-oldest-first').click(function(e) {
      sortBibliography(e, ['date', 'bibliographicCitation'], ['asc', 'asc']);
      $(this).addClass('selected_background');
    });

    if($('#c-map').length) {
      map.initialize();
    }
  }

  function sortBibliography(e, fields, order) {
    e.preventDefault();
    var sorted = {}
    if(bibliography.accepted) {
      sorted['accepted'] = _.orderBy(bibliography.accepted, fields, order);
    }

    if(bibliography.notAccepted) {
      sorted['notAccepted'] = _.orderBy(bibliography.notAccepted, fields, order);
    }

    if(bibliography.liturature) {
      sorted['liturature'] = _.mapValues(bibliography.liturature, function(obj) {
        return _.orderBy(obj, fields, order);
      })
    }

    $('.bibliography-dropdown a').removeClass('selected_background');
    $('#bibliography-citations').html(bibliographyTmpl({
      bibliography: sorted
    }));
  }

  return {
    initialize: initialize,
  }
});
