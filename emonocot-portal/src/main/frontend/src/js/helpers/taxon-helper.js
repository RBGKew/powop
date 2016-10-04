define(['handlebars'], function(Handlebars) {
  Handlebars.registerHelper('color-theme', function(taxon) {
    if(taxon.accepted) {
      return "s-theme-" + taxon.rank;
    } else {
      return "s-theme-Synonym";
    }
  });
});
