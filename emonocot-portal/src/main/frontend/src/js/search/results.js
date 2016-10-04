define(function(require) {

  var $ = require('jquery');
  var Handlebars = require('handlebars');
  var imagesHelper = require('helpers/images-helper');
  var pagination = require('libs/pagination');
  var filters = require('./filters');
  var resultsHeader = require('./resultsHeader');
  var resultsTmpl = require('templates/partials/result/results.js');
  var headerTmpl = require('templates/partials/result/results-header.js');
  var itemsTmpl = require('templates/partials/result/results-items.js');
  var paginationTmpl = require('templates/partials/result/results-pagination.js');
  var Cookies = require('libs/js.cookie.js');

  Handlebars.registerPartial('results-header', headerTmpl);
  Handlebars.registerPartial('results-items', itemsTmpl);
  Handlebars.registerPartial('results-pagination', paginationTmpl);

  var update = function(state) {
    $.getJSON("/api/1/search?" + state, function(json) {
        $('.c-results').replaceWith(resultsTmpl(json));
        $('.c-results .container--lines').replaceWith(itemsTmpl(json));
        resultsHeader.showFacetCounts(json.facets);
        paginate(json);
    });

    resultsHeader.showSelectedView();
    resultsHeader.showSelectedFacet(filters.getParam('selectedFacet'));
    resultsHeader.showSelectedSort(filters.getParam('sort'));
  };

  var updateItems = function(state) {
    $.getJSON("/api/1/search?" + state, function(json) {
        $('.c-results .container--lines').replaceWith(itemsTmpl(json));
    });
  }

  function paginate(results) {
    $('.c-pagination').pagination({
      items: results.totalResults,
      itemsOnPage: results.perPage,
      pages: results.totalPages,
      listStyle: 'pagination',
      hrefTextPrefix: '',
      currentPage: filters.getParam('page.number'),
      onPageClick: function(page, e) {
        filters.setParam('page.number', page);
        e.preventDefault();
      }
    });
  }

  return {
    update : update,
    updateItems: updateItems
  };
});
