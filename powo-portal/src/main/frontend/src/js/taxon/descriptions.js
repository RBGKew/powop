define(function(require) {
  var $ = require('jquery');
  var Autolinker = require('libs/autolinker')

  $('.descriptions-concept-source').each(function () {
    $source = $(this)
    $source.html(Autolinker.link($source.html(), { stripPrefix: false }))
  })
})