define(function(require) {
  var $ = require('jquery'),
    pubsub = require('libs/pubsub'),
    events = require('./events'),
    helper = require('helpers/autocomplete'),
    autocomplete = require('templates/partials/search/autocomplete.js');

  function ac() {
    return $('.tab-pane.active div.c-autocomplete');
  }

  function makeSelection($element) {
    if(!$element.hasClass('selected')) {
      $element.parent().siblings().children('a').removeClass('selected');
      $element.addClass('selected');
    }
  }

  function handleClick(event){
    event.preventDefault();
    makeSelection($(this));
    publishUpdated(events.CLICK);
    hide();
  }

  function handleInput(event) {
    search($(this).val(), getData("suggester"), $(this), getData("showsuggester"), getData("count"));
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

  function handleMouseenter(event) {
    makeSelection($(this), false);
  }

  function getData(key) {
    return $('.autocomplete-form .tab-pane.active input.refine').data(key);
  }

  function publishUpdated(event) {
    pubsub.publish('autocomplete.selected', {
      selected: currentSelection(),
      event: event
    });
  }

  $(document).ready(function() {
    $('.autocomplete-form').on({
      keydown: handleKeydown,
      input: handleInput
    }, 'input.refine');

    $('.autocomplete-form').on({
      click: handleClick,
      mouseenter: handleMouseenter
    }, '.c-autocomplete a');
  });

  var search = function(query, suggesters, $input, showSuggester, pageSize) {
    if(query.length > 1) {
      $.getJSON('/api/1/suggest', {query: query, page_size : pageSize, suggester: suggesters }, function(data) {
        hide();
        $input.after(autocomplete({
          suggestions: data.suggestedTerms,
          theme: 'c-autocomplete--inline',
          showSuggester: showSuggester,
        }));
      });
    }
    else {
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
    publishUpdated(events.DOWN_ARROW);
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
    publishUpdated(events.UP_ARROW);
  };

  var currentSelection = function() {
    console.log(ac().find('a.selected').data("suggestion"));
    return ac().find('a.selected').data("suggestion");
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
