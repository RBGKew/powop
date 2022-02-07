define(function () {
  function updateTokenRemoveButtons($tokenfield) {
    const $removeButtons = $tokenfield.siblings(".token").find("a");
    $removeButtons
      .attr("role", "button")
      .attr("aria-description", "Remove search term");
  }

  function updateCopyHelper($tokenfield) {
    const $copyHelper = $tokenfield.siblings("input");
    $copyHelper.attr("aria-hidden", "true");
  }

  return {
    updateTokenRemoveButtons: updateTokenRemoveButtons,
    updateCopyHelper: updateCopyHelper
  };
});
