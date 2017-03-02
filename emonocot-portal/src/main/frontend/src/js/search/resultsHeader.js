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
  }

  function showSelectedView() {
    if(Cookies.get('powop')) {
      $("#" + Cookies.get('powop')).trigger("click");
    }
  }

  function splitFacet(facet){
    var facets = facet.split(",");
    if($.isArray(facets)) {
      facets.forEach( function(item) {
        pickFacet(item);
      });
    } else {
      pickFacet(item);
    }
  }

  function pickFacet(item) {
    if(item == 'is_fungi'){
      $('.rank_facets').html('<use xlink:href="#Fungi-svg"></use>');
    }
    var id = "#" + item;
    $(id).addClass('selectedFacet');
  }

  function showSelectedFacet(facets) {
    if(facets){
      if($.isArray(facets)) {
        facet.forEach(splitFacet);
      } else {
        splitFacet(facets);
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
