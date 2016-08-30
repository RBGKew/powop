define(function(require) {
  var $ = require('jquery');
  var bootstrap = require('bootstrap');
  var pubsub = require('libs/pubsub');

  var autocomplete = require('./autocomplete');
  var events = require('./events');
  var filters = require('./filters');
  var checkboxes = require('./use-search');
  var results = require('./results');

  function active() {
    return $('.c-search .tab-pane.active');
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

  function updateSuggester(event) {
    var suggester = $(this).find(':selected').val().replace(' ', '-').toLowerCase();
    $(this).parent().parent().find('input').data('suggester', suggester);
  }

  pubsub.subscribe('search.updated.filters', function() {
    results.update(filters.toQuery());
  });

  pubsub.subscribe('search.updated.params', function() {
    results.updateItems(filters.toQuery());
  });

  pubsub.subscribe('search.updated', function(_, updateHistory) {
    if(updateHistory) {
      history.pushState(null, null, '/search?' + filters.serialize());
    }
  });

  pubsub.subscribe('autocomplete.selected', function(_, selected) {
    active().find('input.refine').val(selected);
  });

  $(document).ready(function() {
    if(window.location.search.length > 1) {
      filters.deserialize(window.location.search);
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
  });

  return {
    add: function(key, value) { filters.add(key, value); },
    remove: function(key, value) { filters.remove(key); },
  };
});
