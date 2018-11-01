define(['handlebars'], function(Handlebars) {

  var clean = function(caption) {
    return caption.replace(/"/g, "'");
  }

  Handlebars.registerHelper('thumbnailImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.thumbnail + '" title="'+ clean(image.caption) + '" itemprop="thumbnail"/>');
  });

  Handlebars.registerHelper('fullsizeImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.fullsize + '" title="'+ clean(image.caption) + '" />');
  });

  Handlebars.registerHelper('firstThree', function(images, options) {
    var ret = "";

    for(var i = 0, j = images.length; i < Math.min(j, 3); i++) {
      ret = ret + options.fn(images[i]);
    }

    return ret;
  });
});
