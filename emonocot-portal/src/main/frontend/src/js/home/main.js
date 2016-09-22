define([
  'jquery',
  'search/autocomplete',
  'search/events',
  'libs/pubsub'
], function($, autocomplete, events, pubsub) {

  function handleKeypress(event) {
    if(event.which === events.ENTER) {
      pubsub.publish('search.updated');
      event.preventDefault();
    }
  }

  function loadSearchPage(value){
    if(value !== '') {
      var params = $.param({ filters : { Any : value } });
      window.location = '/search?' + params;
    }
  }

  var initialize = function() {
    $('.c-search-home').on('keypress', 'input.refine', handleKeypress)

    pubsub.subscribe('search.updated', function(){
      loadSearchPage($('#search').val());
    });

    pubsub.subscribe('autocomplete.selected', function(_, msg) {
      if(msg.event === events.CLICK && msg.selected !== '') {
        loadSearchPage(msg.selected);
      }
    });
  };

  return {
    initialize: initialize
  };
});
