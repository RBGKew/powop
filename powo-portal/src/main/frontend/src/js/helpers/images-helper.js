define(['handlebars'], function(Handlebars) {

  var clean = function(caption) {
    if (caption) {
      return caption.replace(/"/g, "'");
    }
  }

  Handlebars.registerHelper('thumbnailImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.thumbnail + '" alt="'+ clean(image.caption) + '" />');
  });

  Handlebars.registerHelper('fullsizeImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.fullsize + '" alt="'+ clean(image.caption) + '" />');
  });

  Handlebars.registerHelper('firstThree', function(images, options) {
    var ret = "";

    for(var i = 0, j = images.length; i < Math.min(j, 3); i++) {
      ret = ret + options.fn(images[i]);
    }

    return ret;
  });
});
