define(['handlebars', '../libs/lodash'], function(Handlebars, _) {
  var ranks = [
    "[infrafam.unranked]",
    "[infragen.unranked]",
    "[infrasp.unranked]",
    "[infragen.grex]",
    "c.[infragen.]",
    "infragen.grex",
    "nothosubsect.",
    "nothosubtrib.",
    "supersubtrib.",
    "nothosubgen.",
    "[infragen.]",
    "nothosubsp.",
    "subsubforma",
    "grex_sect.",
    "[infragen]",
    "nothosect.",
    "subgenitor",
    "subsubvar.",
    "supersect.",
    "supertrib.",
    "suprasect.",
    "agamovar.",
    "[epsilon]",
    "gen. ser.",
    "microgen.",
    "nothogrex",
    "nothoser.",
    "nothovar.",
    "superser.",
    "agamosp.",
    "[alpha].",
    "[gamma].",
    "subhybr.",
    "subsect.",
    "subspec.",
    "subtrib.",
    "agglom.",
    "[beta].",
    "convar.",
    "genitor",
    "monstr.",
    "nothof.",
    "subfam.",
    "subgen.",
    "sublus.",
    "subser.",
    "subvar.",
    "f.juv.",
    "Gruppe",
    "modif.",
    "proles",
    "stirps",
    "subsp.",
    "cycl.",
    "forma",
    "group",
    "linea",
    "prol.",
    "sect.",
    "spec.",
    "subf.",
    "trib.",
    "fam.",
    "gen.",
    "grex",
    "lus.",
    "mut.",
    "oec.",
    "psp.",
    "race",
    "ser.",
    "var.",
    "ap.",
    "II.",
    "nm.",
    "f.",
  ];

  function taxonName(taxon) {
    var formatted = '<em lang="la">' + taxon.name + '</em>';
    ranks.forEach(function(rank) {
      if (formatted.indexOf(rank) > -1) {
        formatted = formatted.replace(" " + rank + " ", "</em> " + rank + " <em lang=\"la\">");
      }
    });

    return formatted;
  };

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
    var str = '<a href="' + taxon.url + '">' + taxonName(taxon);
    if(taxon.author) {
      str += ' <cite>' + taxon.author + '</cite></a>';
    }

    return new Handlebars.SafeString(str);
  });

  Handlebars.registerHelper('nameAndAuthor', function(taxon) {
    var str = taxonName(taxon);
    if(taxon.author) {
      str += ' <cite>' + taxon.author + '</cite>';
    }

    return new Handlebars.SafeString(str);
  });
});
