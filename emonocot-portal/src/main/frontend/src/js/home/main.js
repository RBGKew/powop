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

function updateSuggester(event) {
  var suggester = $(this).find(':selected a').text().replace(' ', '-').toLowerCase();
  $(this).parent().parent().find('input').data('suggester', suggester);
}

var initialize = function() {
  $('.c-search-home')
    .on('keypress', 'input.refine', handleKeypress)
    .on('change', '#search', updateSuggester);

    pubsub.subscribe('search.updated', function(){
      loadSearchPage();
    });
};


  return {
      initialize: initialize
  };

});
