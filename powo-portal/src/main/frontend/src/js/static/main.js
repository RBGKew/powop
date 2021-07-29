define(function(require) {

  var $ = require('jquery');
  var filters = require('search/filters');
  require('libs/bootstrap');

  var initialize = function() {

  // setup search box
  filters.initialize();
  filters.tokenfield().on('tokenfield:createtoken', function(e) {
    e.preventDefault();
    window.location = 'results?q=' + e.attrs.value;
  });

  $(document).on('click', '#search-button', function(e) {
    window.location = 'results/?q=' + $('.token-input').val();
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
  }

  return {
    initialize: initialize,
  }
});
