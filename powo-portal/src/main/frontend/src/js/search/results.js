define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Cookies = require('libs/js.cookie.js');
  var Handlebars = require('handlebars');
  var History = require('libs/native.history');
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

  var prepare = function() {
    if(_.isEmpty($('.c-results-outer'))) {
      $('.c-search').append(resultsContainerTmpl());
    }
  }

  var update = function(state) {
    prepare();

    var indicateProgress = _.debounce(function() {
      $('.total-results').addClass('hidden');
      $('.loading').removeClass('hidden');
    }, 100);
    indicateProgress();

    $.getJSON("/api/1/search?" + state, function(json) {
      indicateProgress.cancel();
      json['f'] = filters.getParam('f');
      json['sort'] = filters.getParam('sort') || 'relevance';
      json['layout'] = Cookies.get('powop');
      $('.c-results').replaceWith(resultsTmpl(json));
      $('.c-results .container--lines').replaceWith(itemsTmpl(json));
      $('.total-results').removeClass('hidden');
      $('.loading').addClass('hidden');
      $('#search-filters').html(filtersTmpl(json));
      $('.results-count').replaceWith(countTmpl(json));
      $('.c-results-footer').replaceWith(paginationTmpl(json));
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

  function setCursor(e) {
    if(e) {
      e.preventDefault();
      var cursor = $(e.target).data("cursor");
      var page = $(e.target).data("page");
      filters.setCursor(cursor, page);
      $('html, body').animate({scrollTop: '0px'}, 100);
    }
  }

  function paginate(results) {
    if(results.totalPages > 1) {
      $('#paginate-first a')
        .attr("href", "/?" + filters.serialize({cursor: "*", p: 0}))
        .click(setCursor);

//      if(results.page === 1) {
//        $('#paginate-prev').addClass('disabled');
//      }

      if(results.page < results.totalPages) {
        $('#paginate-next a')
          .attr("href", "/?" + filters.serialize({cursor: results.cursor, p: results.page}))
          .click(setCursor);
      } else {
        $('#paginate-next').addClass('disabled');
      }

//      $('#paginate-prev a').click(function(e) {
//        if(e) e.preventDefault();
//        window.history.back();
//        $('html, body').animate({scrollTop: '0px'}, 100);
//      });

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
