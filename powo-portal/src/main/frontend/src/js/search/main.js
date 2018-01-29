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

  var initialize = function() {
    if ($(window).width() < 992) {
      $("input[type=search]").attr('placeholder', "Search");
    }
    filters.initialize();
    // populate results based on existing query string
    if(window.location.search.length > 1) {
      filters.deserialize(window.location.search, false);
      results.initialize();
      results.update(filters.serialize());
    }

    $('.s-search__fullpage .c-search .token-input').on('input', function(e) {
      results.initialize();
    });

    $('.c-search')
      .on('click', '.facet', toggleFacet)
      .on('click', '.c-results-outer .sort_options', setSort)
      .on('click', '.c-results-outer .search_view', setView);

    $('.s-page').removeClass('invisible');
  };

  // event listeners for updating search results based on filters
  pubsub.subscribe('search.updated', function() {
    results.update(filters.serialize());
    this.History.pushState(null, null, '?' + filters.serialize());
  });

  return {
    initialize: initialize
  };
});
