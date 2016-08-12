define(['./breadcrumbs'], function(breadcrumbs) {

  $(document).ready(function() {
    // click handlers to remove filter
    $('.c-search__filters').on('click', 'span.glyphicon-remove', function() {
      breadcrumbs.remove($(this).data('term'));
    });

    // hanlders to add filter
    $('.c-search .tab-content').on('keypress', 'input', function(event) {
      // enter pressed in the form
      if(event.which == 13) {
        event.preventDefault();
        var key = $(this).parent().parent().find(':selected').text();
        var value = $(this).val();
        breadcrumbs.add(key, value);
      }
    });
  });

  return {
    add: function(key, value) { breadcrumbs.add(key, value); },
    remove: function(key, value) { breadcrumbs.remove(key); },
    search: function(query) { return breadcrumbs.toString(); },
  };
});
