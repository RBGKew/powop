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

  function loadSearchPage(){
    if($('#search').val() !== '') {
      var params = $.param({
        filters : {
          Any : $('#search').val()
        }
      });
      window.location = '/search?' + params;
    }
  }

  var initialize = function() {
    $('.c-search-home')
      .on('keypress', 'input.refine', handleKeypress)

    pubsub.subscribe('search.updated', function(){
      loadSearchPage();
    });
  };

  return {
    initialize: initialize
  };
});
