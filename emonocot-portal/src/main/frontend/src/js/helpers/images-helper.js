define(['handlebars'], function(Handlebars) {
  Handlebars.registerHelper('thumbnailImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.url + '_thumbnail.jpg" title="'+ image.caption + '" itemprop="thumbnail"/>');
  });

  Handlebars.registerHelper('fullsizeImage', function(image, options) {
    return new Handlebars.SafeString('<img src="' + image.url + '_fullsize.jpg" title="'+ image.caption + '" />');
  });
});
