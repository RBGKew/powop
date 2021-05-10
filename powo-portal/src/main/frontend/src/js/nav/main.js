define(function(require) {
  var $ = require('jquery');
  burgerIcon = false
  $(document).ready(function() {
    $('.toggle-nav').click(function(e) {
      if (burgerIcon) {
        $(this).find("use").attr("xlink:href", "#burgericon");
        burgerIcon = false;
      } else {
        $(this).find("use").attr("xlink:href", "#closeicon");
        burgerIcon = true;
      }
      $(this).toggleClass('active');
      $('.top-right-nav ul').toggleClass('active');

      e.preventDefault();
    });
    $('.about-toggle').click(function(e) {
      $('.children').toggleClass('open');
      e.preventDefault();
    });
  });
});