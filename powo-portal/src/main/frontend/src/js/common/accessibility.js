define(['jquery'], function($) {

	return {

    init: function() {

      // Detect tabbing and add a class 'tabbing' to the body - helps with styling just on mouse/keyboard interaction
      $("body").keydown(function (e) {
         if (e.which == 9) {
             $("body").addClass("tabbing");
         }
      });
      // Remove 'tabbing' class when user is interacting with the mouse
      $("body").on('click', function (e) {
          $("body").removeClass("tabbing");
      });
    },

  }
});
