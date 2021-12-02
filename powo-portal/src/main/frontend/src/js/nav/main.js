define(function (require) {
  var $ = require("jquery");
  var currentLink = require("./current-link");
  var expanded = false;

  currentLink.setCurrentLink();

  function updateIcon() {
    $(".toggle-nav > svg > use").attr(
      "xlink:href",
      expanded ? "#closeicon" : "#burgericon"
    );
  }

  function updateAriaExpanded() {
    $(".toggle-nav").attr("aria-expanded", expanded);
  }

  $(function () {
    updateIcon();
    updateAriaExpanded();

    $(".toggle-nav").on("click", function (e) {
      e.preventDefault();

      expanded = !expanded;

      updateIcon();
      updateAriaExpanded();

      $(this).toggleClass("active");
      $(".top-right-nav ul").toggleClass("active");
      $("html").toggleClass("html--overflow-hidden");

      updateAriaExpanded();
    });
    $(".about-toggle").on("click", function (e) {
      $(".children").toggleClass("open");
      e.preventDefault();
    });
  });
});
