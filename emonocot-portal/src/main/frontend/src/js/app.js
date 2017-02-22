require(['jquery', 'libs/bootstrap', 'search', 'taxon'], function($){

  $(document).ready(function() {

    $('[data-toggle="tooltip"]').tooltip();

    $(".c-results-outer").on("click", ".js-show-list", function() {
      $(".c-results-outer").addClass("grid--rows").removeClass("grid--columns");
    });

    $(".c-results-outer").on("click", ".js-show-grid", function() {
      $(".c-results-outer").addClass("grid--columns").removeClass("grid--rows");
    });

    if($('#content-navbar').length > 0) {
      $('#content-navbar').affix({
          offset: { top: $('#content-navbar').offset().top }
      });
    }

    $('.c-select > select').on('change',function() {
        var context = $(this).parent();
        $('.c-select__default > .text', context).text(
          $(this).find(":selected").text()
        );
    });
  });
});
