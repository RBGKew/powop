define(['handlebars'], function(Handlebars) {
  Handlebars.registerHelper('color-theme', function(taxon) {
    if(taxon.accepted) {
      switch(taxon.rank.toLowerCase()) {
        case 'family':
        case 'genus':
        case 'species':
          return "s-theme-" + taxon.rank;
          break;
        case 'subspecies':
        case 'infraspecificname':
        case 'infrasubspecificname':
        case 'variety':
        case 'subvariety':
        case 'form':
        case 'subform':
          return "s-theme-Infraspecific";
      }
    } else {
      return "s-theme-Synonym";
    }
  });

  Handlebars.registerHelper('taxonLink', function(taxon) {
    return new Handlebars.SafeString('<a href="' + taxon.url + '">' + taxon.name + ' <cite>' + taxon.author + '</cite></a>');
  });

  Handlebars.registerHelper('nameAndAuthor', function(taxon) {
    return new Handlebars.SafeString(taxon.name + ' <cite>' + taxon.author + '</cite>');
  });
});
