define(function(require) {

  var $ = require('jquery');
  var filters = require('search/filters');
  require('libs/bootstrap');

  var initialize = function() {

  // setup search box
  filters.initialize();
  filters.tokenfield().on('tokenfield:createtoken', function(event) {
    event.preventDefault();
    window.location = './results?q=' + e.attrs.value;
  });

  $(document).on('click', '#search-button', function(event) {
    event.preventDefault();
    if ($('.token-input').val()) {
      window.location = './results?q=' + $('.token-input').val();
    }
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
