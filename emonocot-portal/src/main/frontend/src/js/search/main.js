define(function(require) {
  var $ = require('jquery');
  var bootstrap = require('bootstrap');
  var pubsub = require('libs/pubsub');
  var autocomplete = require('./autocomplete');
  var events = require('./events');
  var filters = require('./filters');
  var checkboxes = require('./use-search');
  var results = require('./results');
  var history = require('libs/native.history');
  var Cookies = require('libs/js.cookie.js');

  function active() {
    return $('.autocomplete-form .tab-pane.active');
  }

  function selectType() {
    return active().find('option:selected').val();
  }

  function inputType() {
    return active().data('search-key');
  }

  function filterValue() {
    return active().find('input.refine').val();
  }

  function filterType() {
    var $active = active();

    var lookup = {
      'any' : inputType,
      'names' : selectType,
      'characteristics' : selectType,
      'locations' : inputType,
    }

    return lookup[$active.prop('id')]($active);
  };

  function handleKeypress(event) {
    if(event.which === events.ENTER) {
      if(filterValue() !== '') {
        filters.add(filterType(), filterValue());
        autocomplete.hide();
      }

      $(this).val('');
      event.preventDefault();
    }
  }

  function setView(event) {
    $(this).parent().parent().find('.selected_background').removeClass('selected_background');
    $(this).addClass('selected_background');
    Cookies.set('powop', $(this).attr("id"), { expires: 1095 , path: '' });
  }

  function setFacet(event) {
    event.preventDefault();
    if($(this).attr("id") == 'all_results') {
      $('.facets').removeClass('selectedFacet');
      $(this).addClass('selectedFacet');
    } else {
      $('#all_results').removeClass('selectedFacet');
      $(this).toggleClass('selectedFacet');
    }

    if($('.selectedFacet').length > 1) {
      filters.setParam("selectedFacet", "accepted_names_and_has_images");
    } else {
      filters.setParam("selectedFacet", $(".selectedFacet").attr("id"));
    }
  }

  function setSort(event) {
    event.preventDefault();
    filters.setParam("sort", $(this).attr("id"));
  }

  function updateSuggester(event) {
    var suggester = $(this).find(':selected').data('suggester');
    $(this).parent().parent().find('input').data('suggester', suggester);
  }

  var initialize = function() {
    // populate results based on existing query string
    if(window.location.search.length > 1) {
      filters.deserialize(window.location.search);
    } else {
      results.update(filters.toQuery());
    }

    // handle location hash with tabs
    if(location.hash.slice(1) != "") {
      $('.nav-tabs a[href="' + location.hash + '"]').tab('show');
    }

    $('a[data-toggle="tab"]').on('click', function(e) {
      location.hash = $(e.target).attr('href').substr(1);
    });

    // click handlers to remove filter
    $('.c-search__filters').on('click', '.filter', function() {
      filters.remove($(this));
    });

    $('.c-search')
      .on('keypress', 'input.refine', handleKeypress)
      .on('change', '#names .c-select', updateSuggester);

    $('.c-results-outer')
      .on('click', '.facets', setFacet)
      .on('click', '.sort_options', setSort)
      .on('click', '.search_view', setView);
  };

  // event listeners for updating search results based on filters
  pubsub.subscribe('search.updated', function(_, updateHistory) {
    results.update(filters.toQuery());
    if(updateHistory) {
      History.pushState(null, null, '/search?' + filters.serialize());
    }
  });

  pubsub.subscribe('autocomplete.selected', function(_, msg) {
    if(msg.event === events.CLICK) {
      filters.add(filterType(), msg.selected);
      active().find('input.refine').val('');
    } else {
      active().find('input.refine').val(msg.selected);
    }
  });

  return {
    add: function(key, value) { filters.add(key, value); },
    remove: function(key, value) { filters.remove(key); },
    initialize: initialize
  };
});
