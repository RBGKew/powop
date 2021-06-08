define(function(require) {
  var $ = require('jquery');
  var Autolinker = require('libs/autolinker')
  var bibliographyTmpl = require('templates/partials/taxon/bibliography.js');

  function initialize() {
    // Set up bibliography sort select
    $('#sort-bibliography-by-citation').on('click', function(e) {
      sortBibliography(e, ['bibliographicCitation'], ['asc']);
      $(this).addClass('selected_background');
    });

    $('#sort-bibliography-by-newest-first').on('click', function(e) {
      sortBibliography(e, ['date', 'bibliographicCitation'], ['desc', 'asc']);
      $(this).addClass('selected_background');
    });

    $('#sort-bibliography-by-oldest-first').on('click', function(e) {
      sortBibliography(e, ['date', 'bibliographicCitation'], ['asc', 'asc']);
      $(this).addClass('selected_background');
    });
    // Convert bibliography URLs into links
    linkBibliography()
  }

  function sortBibliography(e, fields, order) {
    e.preventDefault();
    var sorted = {}
    if(bibliography.accepted) {
      sorted['accepted'] = _.orderBy(bibliography.accepted, fields, order);
    }

    if(bibliography.notAccepted) {
      sorted['notAccepted'] = _.orderBy(bibliography.notAccepted, fields, order);
    }

    if(bibliography.liturature) {
      sorted['liturature'] = _.mapValues(bibliography.liturature, function(obj) {
        return _.orderBy(obj, fields, order);
      })
    }

    $('.bibliography-dropdown a').removeClass('selected_background');
    $('#bibliography-citations').html(bibliographyTmpl({
      bibliography: sorted
    }));
    linkBibliography()
  }

  function linkBibliography() {
    // Convert bibliography URLs into links
    $('#bibliography-citations .bibliographic-citation').each(function () {
      $citation = $(this)
      $citation.html(Autolinker.link($citation.html(), { stripPrefix: false }))
    })
  }

  return {
    initialize: initialize
  }
})
