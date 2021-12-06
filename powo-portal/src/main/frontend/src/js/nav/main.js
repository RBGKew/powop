define(function (require) {
  var $ = require("jquery");
  var currentLink = require("./current-link");
  var expanded = false;

  currentLink.setCurrentLink();

  function updateNavigationUi(expanded) {
    $("html").toggleClass("html--overflow-hidden", expanded);
    $(".toggle-nav").toggleClass("active", expanded);
    $(".top-right-nav ul").toggleClass("active", expanded);
    $(".toggle-nav > svg > use").attr(
      "xlink:href",
      expanded ? "#closeicon" : "#burgericon"
    );
  }

  function updateAccessibilityAttributes(expanded) {
    $(".toggle-nav").attr("aria-expanded", expanded);
    $(".toggle-nav .icon").attr(
      "aria-label",
      expanded ? "Close navigation" : "Open navigation"
    );
  }

  $(function () {
    updateNavigationUi(expanded);
    updateAccessibilityAttributes(expanded);

    $(".toggle-nav").on("click", function (e) {
      e.preventDefault();

      expanded = !expanded;

      updateNavigationUi(expanded);
      updateAccessibilityAttributes(expanded);
    });
    $(".about-toggle").on("click", function (e) {
      e.preventDefault();
      $(".children").toggleClass("open");
    });
  });
});
