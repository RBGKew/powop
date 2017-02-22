define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Handlebars = require('handlebars');
  var pagination = require('libs/pagination');
  var filters = require('./filters');
  var resultsHeader = require('./resultsHeader');
  var Cookies = require('libs/js.cookie.js');
  var events = require('./events');

  var resultsContainerTmpl = require('templates/partials/result/results-container.js');
  var resultsTmpl = require('templates/partials/result/results.js');
  var headerTmpl = require('templates/partials/result/results-header.js');
  var itemsTmpl = require('templates/partials/result/results-items.js');
  var paginationTmpl = require('templates/partials/result/results-pagination.js');

  var imagesHelper = require('helpers/images-helper');
  var taxonHelper = require('helpers/taxon-helper.js');

  Handlebars.registerPartial('results-container', resultsContainerTmpl);
  Handlebars.registerPartial('results', resultsTmpl);
  Handlebars.registerPartial('results-header', headerTmpl);
  Handlebars.registerPartial('results-items', itemsTmpl);
  Handlebars.registerPartial('results-pagination', paginationTmpl);

  $(document).on('keydown', function (event) {
    if($(event.target).is('input')) return;

    var pag = $('.c-pagination');
    if(event.which === events.LEFT_ARROW) {
      pag.pagination('prevPage');
    } else if (event.which === events.RIGHT_ARROW) {
      pag.pagination('nextPage');
    }
  });

  var prepare = function() {
    if(_.isEmpty($('.c-results-outer'))) {
      $('.c-search').append(resultsContainerTmpl());
      $('.c-footer').show();
    }
  }

  var update = function(state) {
    prepare();
    resultsHeader.showSelectedView();
    resultsHeader.showSelectedFacet(filters.getParam('f'));
    resultsHeader.showSelectedSort(filters.getParam('sort'));

    $.getJSON("/api/1/search?" + state, function(json) {
      $('.c-results').replaceWith(resultsTmpl(json));
      $('.c-results .container--lines').replaceWith(itemsTmpl(json));
      resultsHeader.showFacetCounts(json.facets);
      paginate(json);
      filters.refresh();
    });
  };

  var updateItems = function(state) {
    $.getJSON("/api/1/search?" + state, function(json) {
      prepare();

      $('.c-results .container--lines').replaceWith(itemsTmpl(json));
      filters.refresh();
    });
  }

  var initialize = function(initialToken) {
    if($('.s-page').hasClass('s-search__fullpage')) {
      $('.s-page').removeClass('s-search__fullpage');
      $('.c-footer').hide();
    }
  }

  function paginate(results) {
    $('.c-pagination').pagination({
      items: results.totalResults,
      itemsOnPage: results.perPage,
      pages: results.totalPages,
      listStyle: 'pagination',
      hrefTextPrefix: '',
      currentPage: Number(filters.getParam('page'))+1,
      onPageClick: function(page, e) {
        filters.setParam('page', page-1);
        if(e) e.preventDefault();
      }
    });
  }

  return {
    initialize: initialize,
    update : update,
    updateItems: updateItems
  };
});
