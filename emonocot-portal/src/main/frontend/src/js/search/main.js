define(function(require) {
  var $ = require('jquery');
  var bootstrap = require('libs/bootstrap');
  var Cookies = require('libs/js.cookie.js');
  var History = require('libs/native.history');
  var pubsub = require('libs/pubsub');

  var events = require('./events');
  var filters = require('./filters');
  var results = require('./results');
  require('libs/bootstrap-tokenfield.js');
  require('libs/bootstrap-cookie-consent.js');
  function active() {
    return $('.autocomplete-form');
  }

  function setView(event) {
    $(this).parent().parent().find('.selected_background').removeClass('selected_background');
    $(this).addClass('selected_background');
    Cookies.set('powop', $(this).attr("id"), { expires: 1095 , path: '' });
  }

  function setFacet(event) {
    event.preventDefault();
    if($(this).attr('id') == 'all_results') {
      $('.facets').removeClass('selectedFacet');
      $(this).addClass('selectedFacet');
      filters.setParam('f', 'all_results');
      $('.rank_facets').html('<use xlink:href="#Plantae-svg"></use>');
    } else {
      $('#all_results').removeClass('selectedFacet');
      if($(this).attr('id') == 'is_fungi'){
        if($(this).hasClass('selectedFacet')){
          $('.rank_facets').html('<use xlink:href="#Plantae-svg"></use>');
        }else{
          $('.rank_facets').html('<use xlink:href="#Fungi-svg"></use>');
        }
      }
      $(this).toggleClass('selectedFacet');
      var facets = [];
      $('.selectedFacet').each(function() {
        facets.push(this.id);
      });
      if(_.isEmpty(facets)) {
        filters.removeParam('f');
      } else {
        filters.setParam('f', facets.join(','));
      }
    }
  }

  function setSort(event) {
    event.preventDefault();
    filters.setParam('sort', $(this).attr("id"));
  }

  var initialize = function() {
    if ($(window).width() < 992) {
        $("input[type=search]").attr('placeholder', "Search");
    }
    filters.initialize();
    // populate results based on existing query string
    if(window.location.search.length > 1) {
      results.initialize();
      filters.deserialize(window.location.search);
    }

    $('.s-search__fullpage .c-search .token-input').on('input', function(e) {
      results.initialize();
    });

    $('.c-search')
      .on('click', '.c-results-outer .facets', setFacet)
      .on('click', '.c-results-outer .sort_options', setSort)
      .on('click', '.c-results-outer .search_view', setView);

    $('.s-page').removeClass('invisible');
  };

  // event listeners for updating search results based on filters
  pubsub.subscribe('search.updated', function() {
    console.log(filters.serialize());
    results.update(filters.serialize());
    this.History.pushState(null, null, '?' + filters.serialize());
  });

  return {
    initialize: initialize
  };
});
