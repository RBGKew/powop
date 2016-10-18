define(['handlebars'], function(Handlebars) {
  Handlebars.registerHelper('color-theme', function(taxon) {
    if(taxon.accepted) {
      return "s-theme-" + taxon.rank;
    } else {
      return "s-theme-Synonym";
    }
  });

  Handlebars.registerHelper('taxon-link', function(taxon) {
    return new Handlebars.SafeString('<a href="' + taxon.url + '">' + taxon.name + ' <cite>' + taxon.author + '</cite></a>');
  });

  Handlebars.registerHelper('taxon-name', function(taxon) {
    return new Handlebars.SafeString(taxon.name + ' <cite>' + taxon.author + '</cite>');
  });
});
