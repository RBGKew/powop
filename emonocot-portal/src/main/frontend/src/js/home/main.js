define([
'jquery',
'search/autocomplete',
'search/events'
], function($, autocomplete, events) {

function handleKeypress(event) {
  if(event.which === events.ENTER) {
    loadSearchPage()
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

  $.getJSON("/api/1/search?", function(json) {

  });
};


  return {
      initialize: initialize
  };

});
