require([
  'search',
  'taxon'
]);

console.log("If you want to use POWO data programatically, please check out our python client https://github.com/RBGKew/pykew");

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
});