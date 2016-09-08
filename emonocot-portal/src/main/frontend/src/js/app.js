require(['jquery', 'search', 'bootstrap'], function($, search){

  $(document).ready(function() {

    $('body').scrollspy({ target: '#content-navbar' });

    $('[data-toggle="tooltip"]').tooltip();

    $('[data-toggle="popover"]').popover()

    $(".c-results-outer").on("click", ".js-show-list", function() {
      $(".grid").addClass("grid--rows").removeClass("grid--columns");
    });

    $(".c-results-outer").on("click", ".js-show-grid", function() {
      $(".grid").addClass("grid--columns").removeClass("grid--rows");
    });

    if($('#content-navbar').length > 0) {
      $('#content-navbar').affix({
          offset: {
              top: $('#content-navbar').offset().top
          }
      });
    }

    $('.c-select > select').on('change',function() {
        var context = $(this).parent();
        $('.c-select__default > .text', context).text(
          $(this).find(":selected").text()
        );
    });

//    $("input#search").on("focus", function() {
//      $("body").addClass("js-autocomplete js-fade-background");
//    });

//    $("input#search").on("blur", function() {
//      $("body").removeClass("js-autocomplete js-fade-background");
//    });

    $("input#refine").on("focus", function() {
      $("body").addClass("js-autocomplete");
    });

    $("input#refine").on("blur", function() {
      $("body").removeClass("js-autocomplete");
    });
  });

});
