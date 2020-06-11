define(function(require) {
  var $ = require('jquery');
  var _ = require('libs/lodash');
  var bootstrap = require('libs/bootstrap');
  var Cookies = require('libs/js.cookie.js');
  var History = require('libs/native.history');
  var pubsub = require('libs/pubsub');

  var events = require('./events');
  var filters = require('./filters');
  var results = require('./results');
  require('libs/bootstrap-tokenfield.js');
  require('libs/bootstrap-cookie-consent.js');

  function setView(event) {
    $(this).parent().parent().find('.selected_background').removeClass('selected_background');
    $(this).addClass('selected_background');
    Cookies.set('powop', $(this).attr("id"), { expires: 1095 , path: '' });
  }

  function toggleFacet(event) {
    event.preventDefault();
    filters.toggleFacet($(this).data('facet'));
  }

  function setSort(event) {
    event.preventDefault();
    filters.setSort($(this).attr("id"));
  }

  function transformToSearchLayout() {
    // transform page to search style page
    if($('.s-page').hasClass('s-search__fullpage')) {
      results.initialize();
      $('.s-page').removeClass('s-search__fullpage');
      $('#search_box').detach().appendTo('.c-header .container');
      filters.refresh();
    }
  }
  
  var is_hashed = false;

  $(window).on('hashchange', function() {
      is_hashed = true;
  });


  // window.addEventListener('popstate', syncWithUrl);
  
  window.addEventListener('popstate', function(e){
    // if hashchange
    if (is_hashed) {
        e.preventDefault();
        // reset
        is_hashed = false;
        return false;
    }
    syncWithUrl
});

  function syncWithUrl() {
    filters.deserialize(window.location.search, false);
    filters.refresh();
    results.update(filters.serialize());
  }

  var initialize = function() {
    if ($(window).width() > 992) {
      $("input[type=search]").attr('placeholder', "Search by species, genus or family name, or any words describing the plant");
    }

    filters.initialize();
    // populate results based on existing query string
    if(window.location.search.length > 1) {
      transformToSearchLayout();
      filters.deserialize(window.location.search, false);
      results.initialize();
      results.update(filters.serialize());
    }

    $(document)
      .on('click', '.facet', toggleFacet)
      .on('click', '.c-results-outer .sort_options', setSort)
      .on('click', '.c-results-outer .search_view', setView)
      .on('click', '#search-button', function(e) {
        var input = $('.token-input');
        filters.add(input.val());
        input.val('');
      });
    $('.s-page').removeClass('invisible');
  };

  // event listeners for updating search results based on filters
  pubsub.subscribe('search.updated', function() {
    transformToSearchLayout();

    results.update(filters.serialize());
    this.History.pushState(null, null, '?' + filters.serialize());
  });

  return {
    initialize: initialize
  };
});
