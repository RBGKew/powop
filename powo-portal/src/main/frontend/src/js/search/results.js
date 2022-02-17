define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Cookies = require('libs/js.cookie.js');
  var Handlebars = require('handlebars');
  var filters = require('./filters');
  var pageTitle = require("./page-title");

  var resultsContainerTmpl = require('templates/partials/result/results-container.js');
  var resultsTmpl = require('templates/partials/result/results.js');
  var headerTmpl = require('templates/partials/result/results-header.js');
  var itemsTmpl = require('templates/partials/result/results-items.js');
  var paginationTmpl = require('templates/partials/result/results-pagination.js');
  var countTmpl = require('templates/partials/result/count.js');
  var filtersTmpl = require('templates/partials/result/results-filters.js');

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
      $('.results-grid').append(resultsContainerTmpl());
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
      $('.filters').replaceWith(filtersTmpl(json));
      $(".results-count").replaceWith(countTmpl(resultsCountData(json)));
      $('.c-results-footer').replaceWith(paginationTmpl(json));
      paginate(json);
      pageTitle.updatePageTitle(window.siteData, filters.filters());
      filters.refresh();
    });
  };

  const sortDescriptions = {
    "relevance": "by relevance",
    "name_asc": "alphabetically ascending",
    "name_desc": "alphabetically descending",
  };
  const filterDescriptions = {
    "accepted_names": "accepted names only",
    "has_images": "has images",
    "family_f": "families",
    "genus_f": "genera",
    "species_f": "species",
    "infraspecific_f": "infraspecifics",
  };
  function resultsCountData(resultsJson) {
    // Filters returned in the format: "species_f,infraspecific_f"
    const filters = (resultsJson.f || "").split(",").filter(function (f) {
      return f.length > 0;
    });
    return {
      totalResults: resultsJson.totalResults,
      sortDescription: sortDescriptions[resultsJson.sort],
      filterDescriptions: filters.map(function (f) {
        return filterDescriptions[f];
      }),
    };
  }

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
    $(".grid").addClass("grid--rows");
    $(".js-view-mode").text("Viewing in list mode");
  }

  function gridView() {
    $(".grid").removeClass("grid--rows");
    $(".js-view-mode").text("Viewing in grid mode");
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
        .on("click", setCursor);

      if(results.page < results.totalPages) {
        $('#paginate-next a')
          .attr("href", "/?" + filters.serialize({cursor: results.cursor, p: results.page}))
          .on("click", setCursor);
      } else {
        $('#paginate-next').addClass('disabled');
      }

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
