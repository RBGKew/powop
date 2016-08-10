require(['jquery', 'bootstrap'], function($){

  $(document).ready(function() {

    $('body').scrollspy({ target: '#content-navbar' });

    $('[data-toggle="tooltip"]').tooltip();

    $('[data-toggle="popover"]').popover()

    $(".js-show-list").on("click", function() {
      $(".grid").addClass("grid--rows").removeClass("grid--columns");
    });

    $(".js-show-grid").on("click", function() {
      $(".grid").addClass("grid--columns").removeClass("grid--rows");
    });

    if($('#content-navbar').length > 0) {
      $('#content-navbar').affix({
          offset: {
              top: $('#content-navbar').offset().top
          }
      });
    }


  });

});