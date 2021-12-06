define(function (require) {
  var $ = require("jquery");
  var currentLink = require("./current-link");
  var expanded = false;

  currentLink.setCurrentLink();

  function updateNavigationUi() {
    $(".toggle-nav > svg > use").attr(
      "xlink:href",
      expanded ? "#closeicon" : "#burgericon"
    );
  }

  function updateAccessibilityAttributes() {
    $(".toggle-nav").attr("aria-expanded", expanded);
    $(".toggle-nav .icon").attr(
      "aria-label",
      expanded ? "Close navigation" : "Open navigation"
    );
  }

  $(function () {
    updateNavigationUi();
    updateAccessibilityAttributes();

    $(".toggle-nav").on("click", function (e) {
      e.preventDefault();

      expanded = !expanded;

      updateNavigationUi();
      updateAccessibilityAttributes();

      $(this).toggleClass("active");
      $(".top-right-nav ul").toggleClass("active");
      $("html").toggleClass("html--overflow-hidden");
    });
    $(".about-toggle").on("click", function (e) {
      $(".children").toggleClass("open");
      e.preventDefault();
    });
  });
});
