define(['handlebars', 'libs/lodash'], function(Handlebars, _) {
  Handlebars.registerHelper('color-theme', function(taxon) {
    if(taxon.accepted) {
      switch(_.toLower(taxon.rank)) {
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
        case '':
          return "s-theme-Infraspecific";
      }
    } else {
      return "s-theme-Synonym";
    }
  });

  Handlebars.registerHelper('taxonLink', function(taxon) {
    var str = '<a href="' + taxon.url + '">' + taxon.name;
    if(taxon.author) {
      str += ' <cite>' + taxon.author + '</cite></a>';
    }

    return new Handlebars.SafeString(str);
  });

  Handlebars.registerHelper('nameAndAuthor', function(taxon) {
    var str = taxon.name;
    if(taxon.author) {
      str += ' <cite>' + taxon.author + '</cite>';
    }

    return new Handlebars.SafeString(str);
  });
});
