/**
 * Module for managing gallery behaviour on taxon page. Uses MagnificPopup jQuery plugin, with additional
 * logic for keeping accessibility related states in sync.
 * 
 * See https://dimsemenov.com/plugins/magnific-popup/documentation.html for MagnificPopup documentation
 */
define(function (require) {
  var $ = require("jquery");
  require("libs/magnific-popup");

  function toggleAriaExpanded(galleryItem, expanded) {
    // the galleryItem from magnificPopup.items can either be a custom object with a jQuery
    // `el` or it can be a DOM element, so we need to handle it accordingly
    $(galleryItem.el || galleryItem).attr("aria-expanded", expanded);
  }

  function initialize() {
    var headerImage = $(".c-gallery-header");

    headerImage.on("click", function (e) {
      $(".c-gallery").magnificPopup("open");
      toggleAriaExpanded(this, true);
      e.preventDefault();
    });

    $(".c-gallery").magnificPopup({
      delegate: "a",
      type: "image",
      image: {
        titleSrc: "data-caption",
      },
      closeMarkup:
        '<button aria-label="Close gallery" type="button" class="mfp-close">&#215;</button>',
      gallery: { enabled: true },
      callbacks: {
        open: function () {
          $(".mfp-figure figcaption").attr("aria-live", "polite");
        },
        close: function () {
          this.items.forEach(function (item) {
            toggleAriaExpanded(item, false);
          });

          toggleAriaExpanded(headerImage, false);
        },
        change: function () {
          this.items.forEach(
            function (item) {
              toggleAriaExpanded(item, this.currItem === item);
            }.bind(this)
          );

          var currSrc = this.currItem.src;
          var headerImgSrc = headerImage.find("img").attr("src");
          toggleAriaExpanded(headerImage, currSrc === headerImgSrc);
        },
      },
    });
  }

  return {
    initialize: initialize,
  };
});
