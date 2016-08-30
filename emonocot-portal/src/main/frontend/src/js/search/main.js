define([
  'jquery',
  'bootstrap',
  './autocomplete',
  './events',
  './filters',
  './use-search',
  'libs/pubsub',
  './results'
], function($, bootstrap, autocomplete, events, filters, checkboxes, pubsub, results) {

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

  function selectFacet(event) {
    filters.setParam("selectedFacet", $(this).attr("id"));
  }

  function selectSort(event) {
    filters.setParam("sort", $(this).attr("id"));
  }

  function updateSuggester(event) {
    var suggester = $(this).find(':selected').val().replace(' ', '-').toLowerCase();
    $(this).parent().parent().find('input').data('suggester', suggester);
  }

  pubsub.subscribe('search', function(_, selected){
    results.update(filters.toString());
  });

  $(document).ready(function() {

    results.update(filters.toString());
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
      .on('click', '.facets', selectFacet)
      .on('click', '.sort_options', selectSort);
  });

  pubsub.subscribe('autocomplete.selected', function(_, selected) {
    active().find('input.refine').val(selected);
  });

  return {
    add: function(key, value) { filters.add(key, value); },
    remove: function(key, value) { filters.remove(key); },
  };
});
