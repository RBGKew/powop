define(['handlebars'], function(Handlebars) {
  Handlebars.registerHelper('thumbnailImage', function(image, options) {
    if(image.url.indexOf("https://dams.kew.org") !== -1){
      return new Handlebars.SafeString('<img src="' + image.url + '?s=400&k=131f04e3b359a15762abfab29c7001d9" title="'+ image.caption + '" itemprop="thumbnail"/>');
    }


    return new Handlebars.SafeString('<img src="' + image.url + '_thumbnail.jpg" title="'+ image.caption + '" itemprop="thumbnail"/>');
  });

  Handlebars.registerHelper('fullsizeImage', function(image, options) {
    if(image.url.indexOf("https://dams.kew.org") !== -1){
      return new Handlebars.SafeString('<img src="' + image.url + '?s=1600&k=fe543868fc853b0d4698dcd2abfdbcfb" title="'+ image.caption + '" itemprop="thumbnail"/>');
    }
    return new Handlebars.SafeString('<img src="' + image.url + '_fullsize.jpg" title="'+ image.caption + '" />');
  });

  Handlebars.registerHelper('firstThree', function(images, options) {
    var ret = "";

    for(var i = 0, j = images.length; i < Math.min(j, 3); i++) {
      ret = ret + options.fn(images[i]);
    }

    return ret;
  });
});
