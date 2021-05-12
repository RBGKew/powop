define(function(require) {
  var $ = require('jquery');
  var current = location.pathname;
  console.log(current)
  $('.top-right-nav li a').each(function(){
      var $this = $(this);
      if($this.attr('href').indexOf(current) !== -1){
          $this.addClass('current');
      }
  })
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
      $('html').toggleClass('html--overflow-hidden');

      e.preventDefault();
    });
    $('.about-toggle').click(function(e) {
      $('.children').toggleClass('open');
      e.preventDefault();
    });
  });
});