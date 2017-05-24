require(['jquery', 'libs/bootstrap', 'search', 'taxon', 'admin'], function($){

  $(document).ready(function() {

    $('[data-toggle="tooltip"]').tooltip();

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
