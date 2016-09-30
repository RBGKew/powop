define(function(require) {
  var $ = require('jquery');
  var Cookies = require('libs/js.cookie.js');

  function showSelectedSort(sort){
    $(".sort_options").removeClass('selected_background')
    $("#" + sort).addClass('selected_background');
  }

  function showFacetCounts(facets){
    $('#all_results').find('span').replaceWith(facets.all_results);
    $('#accepted_names').find('span').replaceWith(facets.accepted_names);
    $('#has_images').find('span').replaceWith(facets.has_images);
  }

  function showSelectedView(){
    if(Cookies.get('powop')){
      $("#" + Cookies.get('powop')).trigger("click");
    }
  }

  function showSelectedFacet(selectedFacet){
      switch(selectedFacet) {
      case "accepted_names":
          $("#accepted_names").addClass('selectedFacet');
          break;
      case "has_images":
          $("#has_images").addClass('selectedFacet');
          break;
      case "accepted_names_and_has_images":
          $("#accepted_names").addClass('selectedFacet');
          $("#has_images").addClass('selectedFacet');
          break;
      default:
          $("#all_results").addClass('selectedFacet');
    }
  }


  return {
    showSelectedFacet : showSelectedFacet,
    showFacetCounts : showFacetCounts,
    showSelectedSort : showSelectedSort,
    showSelectedView : showSelectedView
  };
});
