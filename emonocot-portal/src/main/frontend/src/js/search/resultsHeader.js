define(function(require) {
  var $ = require('jquery');
  var Cookies = require('libs/js.cookie.js');

  function showSelectedSort(sort) {
    $(".sort_options").removeClass('selected_background')
    $("#" + sort).addClass('selected_background');
  }

  function showFacetCounts(facets) {
    $('#all_results span').text(facets.all_results);
    $('#accepted_names span').text(facets.accepted_names);
    $('#has_images span').text(facets.has_images);
    $('#is_fungi span').text(facets.is_fungi);
  }

  function showSelectedView() {
    if(Cookies.get('powop')) {
      $("#" + Cookies.get('powop')).trigger("click");
    }
  }

  function pickFacet(facet){
    switch(facet) {
      case "accepted_names":
        $("#accepted_names").addClass('selectedFacet');
        break;
      case "has_images":
        $("#has_images").addClass('selectedFacet');
        break;
      case "is_fungi":
        $("#is_fungi").addClass('selectedFacet');
        break;
      default:
        $("#all_results").addClass('selectedFacet');
    }
  }

  function showSelectedFacet(facets) {
    if(facets){
      if($.isArray(facets)) {
        facets.forEach(pickFacet)
      } else {
        pickFacet(facets);
      }
    } else {
      $('.facets').removeClass('selectedFacet');
      $("#all_results").addClass('selectedFacet');
    }
  }

  return {
    showSelectedFacet : showSelectedFacet,
    showFacetCounts : showFacetCounts,
    showSelectedSort : showSelectedSort,
    showSelectedView : showSelectedView,
  };
});
