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
    Cookies.set('powop', $(this).attr("id"), { expires: 1095 , path: '' });
  }

  function setFacet(event) {
    event.preventDefault();
    filters.setParam("selectedFacet", $(this).attr("id"));
  }

  function setSort(event) {
    event.preventDefault();
    filters.setParam("sort", $(this).attr("id"));
  }

  function updateSuggester(event) {
    var suggester = $(this).find(':selected').text().replace(' ', '-').toLowerCase();
    $(this).parent().parent().find('input').data('suggester', suggester);
  }

  var initialize = function() {
    if(window.location.search.length > 1) {
      filters.deserialize(window.location.search);
    }

    results.update(filters.toQuery());

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

  pubsub.subscribe('search.updated', function(_, updateHistory) {
    results.update(filters.toQuery());
    if(updateHistory) {
      History.pushState(null, null, '/search?' + filters.serialize());
    }
  });

  pubsub.subscribe('autocomplete.selected', function(_, selected) {
    active().find('input.refine').val(selected);
  });

  pubsub.subscribe('autocomplete.selected', function(_, selected) {
    active().find('input.refine').val(selected);
  });

  return {
    add: function(key, value) { filters.add(key, value); },
    remove: function(key, value) { filters.remove(key); },
    initialize: initialize
  };
});
