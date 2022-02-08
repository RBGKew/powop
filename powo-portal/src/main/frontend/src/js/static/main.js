define(function(require) {

  var $ = require('jquery');
  var search = require('search');
  require('libs/bootstrap');

  var initialize = function() {

  // setup search box
  search.initRedirectSearch();
    
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
