define(['./filters', './use-search'], function(filters, checkboxes) {

  function selectKeyLookup(trigger) {
    return $(trigger).parent().parent().find(':selected').val();
  }

  function inputKeyLookup(trigger) {
    return $(trigger).closest('.tab-pane').data('search-key');
  }

  function lookupKey(trigger) {
    var lookupMap = {
      'any' : inputKeyLookup,
      'names' : selectKeyLookup,
      'characteristics' : selectKeyLookup,
      'localities' : inputKeyLookup,
    }

    return lookupMap[$(trigger).closest('.tab-pane').prop('id')](trigger);
  };


  $(document).ready(function() {
    // click handlers to remove filter
    $('.c-search__filters').on('click', 'span.glyphicon-remove', function() {
      filters.remove($(this).parent());
    });

    // hanlders to add filter
    $('.c-search .tab-content').on('keypress', 'input.refine', function(event) {
      // enter pressed in the form
      if(event.which == 13) {
        event.preventDefault();
        var key = lookupKey(this);
        var value = $(this).val();
        filters.add(key, value);
        $(this).val('');
      }
    });
  });

  return {
    add: function(key, value) { filters.add(key, value); },
    remove: function(key, value) { filters.remove(key); },
    search: function(query) { return filters.toString(); },
  };
});
