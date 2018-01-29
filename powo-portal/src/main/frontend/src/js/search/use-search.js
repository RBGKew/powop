define(['jquery', 'libs/pubsub', './filters'], function($, pubsub, filters) {
    $('#uses #select-all').on('click', function() {
      $('.checkbox-group input[type="checkbox"]').prop('checked', true)
      filters.set('Use', 'Any');
    });

    $('#uses #deselect-all').on('click', function() {
      $('.checkbox-group input[type="checkbox"]').prop('checked', false)
      filters.remove($('button.Use'))
    });

    $('#uses form').on('change', 'input[type="checkbox"]', function() {
      var checked = $('.checkbox-group input:checked').map(function() { return $(this).val(); })
      filters.set('Use', $.makeArray(checked));
    });

    pubsub.subscribe('search.filters.Use', function(_, uses) {
      $('.checkbox-group input[type="checkbox"]').each(function(index, checkbox) {
        if(uses.indexOf(checkbox.value) > -1 || uses.indexOf('Any') > -1) {
          $(checkbox).prop('checked', true);
        } else {
          $(checkbox).prop('checked', false);
        }
      });
    });
});

