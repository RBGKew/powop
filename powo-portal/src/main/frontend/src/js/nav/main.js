define(function(require) {
  var $ = require('jquery');
  var currentLink = require('./current-link');

  currentLink.setCurrentLink();

  burgerIcon = false
  $(document).ready(function() {
    $('.toggle-nav').click(function(e) {
      e.preventDefault();
      if (burgerIcon) {
        $(this).find("use").attr("xlink:href", "#burgericon");
        burgerIcon = false;
      } else {
        $(this).find("use").attr("xlink:href", "#closeicon");
        burgerIcon = true;
      }
      $(this).toggleClass('active');
      $('.top-right-nav ul').toggleClass('active');
      $('html').toggleClass('html--overflow-hidden');
    });
    $('.about-toggle').click(function(e) {
      $('.children').toggleClass('open');
      e.preventDefault();
    });
  });
});