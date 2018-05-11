define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Cookies = require('libs/js.cookie.js');
  var Handlebars = require('handlebars');
  var pagination = require('libs/pagination');
  var filters = require('./filters');
  var events = require('./events');

  var resultsContainerTmpl = require('templates/partials/result/results-container.js');
  var resultsTmpl = require('templates/partials/result/results.js');
  var headerTmpl = require('templates/partials/result/results-header.js');
  var itemsTmpl = require('templates/partials/result/results-items.js');
  var paginationTmpl = require('templates/partials/result/results-pagination.js');
  var countTmpl = require('templates/partials/result/count.js');
  var filtersTmpl = require('templates/partials/result/filters.js');

  require('helpers/images-helper');
  require('helpers/taxon-helper.js');
  require('helpers/search-helper.js');

  Handlebars.registerPartial('results', resultsTmpl);
  Handlebars.registerPartial('results-container', resultsContainerTmpl);
  Handlebars.registerPartial('results-count', countTmpl);
  Handlebars.registerPartial('results-filters', filtersTmpl);
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
    }
  }

  var update = function(state) {
    prepare();
    $.getJSON("/api/1/search?" + state, function(json) {
      json['f'] = filters.getParam('f');
      json['sort'] = filters.getParam('sort') || 'relevance';
      json['layout'] = Cookies.get('powop');
      $('.c-results').replaceWith(resultsTmpl(json));
      $('.c-results .container--lines').replaceWith(itemsTmpl(json));
      $('#search-filters').html(filtersTmpl(json));
      $('.results-count').replaceWith(countTmpl(json));
      paginate(json);
      filters.refresh();
    });
  };

  var initialize = function(initialToken) {
    $('body')
      .on('click', '.js-show-list', listView)
      .on('click', '.js-show-grid', gridView)
      .on('change', '.c-per-page', setPageSize);

    if($(window).width() < 992) {
      $(document).scrollTop( $("#search_box").offset().top);
    }
  }

  function setPageSize(e) {
    e.preventDefault();
    filters.setPageSize($(this).val());
  }

  function listView() {
    $(".c-results-outer").addClass("grid--rows").removeClass("grid--columns");
  }

  function gridView() {
    $(".c-results-outer").addClass("grid--columns").removeClass("grid--rows");
  }

  function paginate(results) {
    if(results.totalPages > 1) {
      $('.c-pagination').pagination({
        items: results.totalResults,
        itemsOnPage: results.perPage,
        pages: results.totalPages,
        listStyle: 'pagination',
        hrefTextPrefix: '',
        currentPage: Number(filters.getParam('page'))+1,
        onPageClick: function(page, e) {
          filters.setPage(page-1);
          $('html, body').animate({scrollTop: '0px'}, 100);
          if(e) e.preventDefault();
        }
      });
      $('.c-results-footer').removeClass('hidden');
    }

    if(results.totalResults > 24) {
      $('.c-results-footer').removeClass('hidden');
      $('.c-per-page').removeClass('hidden');
    }
  }

  return {
    initialize: initialize,
    update : update,
  };
});
