define(function(require) {
  var $ = require('jquery');
  var Autolinker = require('libs/autolinker')

  var initialize = function () {
    $('.descriptions-concept-source').each(function () {
      $source = $(this)
      $source.html(Autolinker.link($source.html(), { stripPrefix: false }))
    })
  }

  return {
    initialize: initialize,
  }
})