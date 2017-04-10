define(function(require) {
  var $ = require('jquery');
  var Cookies = require('libs/js.cookie.js');

  function showSelectedSort(sort) {
    $(".sort_options").removeClass('selected_background')
    $("#" + sort).addClass('selected_background');
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
    var selector = '.facet.' + item ;
    $(selector).addClass('selected');
  }

  function showSelectedFacet(facets) {
    if(facets) {
      if($.isArray(facets)) {
        facet.forEach(splitFacet);
      } else {
        splitFacet(facets);
      }
    }
  }

  return {
    showSelectedFacet : showSelectedFacet,
    showSelectedSort : showSelectedSort,
    showSelectedView : showSelectedView,
  };
});
