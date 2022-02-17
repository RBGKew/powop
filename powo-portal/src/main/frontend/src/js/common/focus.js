define(function (require) {
  /** Selectors for elements that can be focused */
  const FOCUSABLE = [
    "a[href]",
    "area[href]",
    'input:not([disabled]):not([type="hidden"]):not([aria-hidden])',
    "select:not([disabled]):not([aria-hidden])",
    "textarea:not([disabled]):not([aria-hidden])",
    "button:not([disabled]):not([aria-hidden])",
    "iframe",
    "object",
    "embed",
    "[contenteditable]",
    '[tabindex]:not([tabindex^="-"])',
  ];

  function containFocus(element) {
    /**
     * Based on Vuetensil's VDrawer: https://github.com/Stegosource/vuetensils/blob/master/src/components/VDrawer/VDrawer.vue
     * When the panel is focused, capture focus events and replicate tab/shift-tab functionality
     * to focus the next/previous focusable element.
     */
    function handleKeydown(event) {
      if (event.key === "Tab") {
        const content = element;
        if (!content) return;
        const focusable = Array.from(content.querySelectorAll(FOCUSABLE));
        if (!focusable.length) {
          event.preventDefault();
          return;
        }
        if (!content.contains(document.activeElement)) {
          event.preventDefault();
          focusable[0].focus();
        } else {
          const focusedItemIndex = focusable.indexOf(document.activeElement);
          if (event.shiftKey && focusedItemIndex === 0) {
            focusable[focusable.length - 1].focus();
            event.preventDefault();
          }
          if (!event.shiftKey && focusedItemIndex === focusable.length - 1) {
            focusable[0].focus();
            event.preventDefault();
          }
        }
      }
    }

    element.addEventListener("keydown", handleKeydown);
    element.focus({ preventScroll: true });

    return function destroy() {
      element.removeEventListener("keydown", handleKeydown);
    };
  }

  return { containFocus };
});
