define(function(require) {

  var $ = require('jquery');
  var filters = require('search/filters');
  var date = require('./date');
  require('libs/bootstrap');

  var initialize = function() {

  // setup search box
  filters.initialize();
  filters.tokenfield().on('tokenfield:createtoken', function(e) {
    e.preventDefault();
    window.location = '/?q=' + e.attrs.value;
  });

  $(document).on('click', '#search-button', function(e) {
    window.location = '/?q=' + $('.token-input').val();
  })
    
    
  $('.tokenfield input')
    .on('focus', function() {
      $('#search_box')
        .addClass('focused');
    })
    .on('blur', function() {
      $('#search_box')
        .removeClass('focused');
    })

    $( ".full-year" ).append( date.fullYear );
    $( ".full-date" ).append( date.fullDate );
  }

  return {
    initialize: initialize,
  }
});
