define([
  'jquery',
  'libs/pubsub',
  './events',
  'templates/partials/search/autocomplete.js',
], function($, pubsub, events, autocomplete) {

  function ac() {
    return $('.tab-pane.active div.c-autocomplete');
  }

  function makeSelection($element) {
    if(!$element.hasClass('selected')) {
      $element.parent().siblings().children('a').removeClass('selected');
      $element.addClass('selected');
      pubsub.publish('autocomplete.selected', currentSelection());
    }
  }

  function handleInput(event) {
    search($(this).val(), suggester(), $(this));
  }

  function handleKeydown(event) {
    var $input = $(this);
    if (event.which === events.UP_ARROW) {
      navigateUp($input);
      event.preventDefault();
    } else if (event.which === events.DOWN_ARROW) {
      navigateDown($input);
      event.preventDefault();
    }
  }

  function suggester() {
    return $('.c-search .tab-pane.active input.refine').data('suggester');
  }

  $(document).ready(function() {
    $('.c-search').on({
      keydown: handleKeydown,
      input: handleInput
    }, 'input.refine');

    $('.c-search').on({
      mouseenter: function(e) {
        makeSelection($(this));
      },
      click: function(e) {
        makeSelection($(this));
        hide();
      },
    }, '.c-autocomplete a');
  });

  var search = function(query, suggesters, $input) {
    if(query.length > 1) {
      $.getJSON('/api/1/suggest', {query: query, suggester: suggesters}, function(data) {
        hide();
        $input.after(autocomplete({
          suggestions: data.suggestedTerms,
          theme: 'c-autocomplete--inline'
        }));
      });
    } else {
      hide();
    }
  };

  var hide = function() {
    var existingAutocomplete = ac();
    if(existingAutocomplete) {
      existingAutocomplete.remove();
    }
  };

  var navigateDown = function() {
    var $autocomplete = ac();
    if($autocomplete.length == 0) return;

    var $current = $autocomplete.find('.selected');

    if($current.length == 0) {
      makeSelection($autocomplete.find('a').first());
    } else {
      var $next = $current.parent().next().children('a');
      if($next.length > 0) {
        makeSelection($next);
      }
    }
  };

  var navigateUp = function() {
    var $autocomplete = ac();
    if($autocomplete.length == 0) return;

    var $current = $autocomplete.find('.selected');

    if($current.length == 0) {
      makeSelection($autocomplete.find('a').last());
    } else {
      var $prev = $current.parent().prev().children('a');
      makeSelection($prev);
    }
  };

  var currentSelection = function() {
    return ac().find('a.selected').text();
  };

  var hasSelection = function() {
    return ac().find('.selected').length > 0;
  };

  return {
    search: search,
    hide: hide,
    navigateDown: navigateDown,
    navigateUp: navigateUp,
    currentSelection: currentSelection,
    hasSelection: hasSelection
  };
});
