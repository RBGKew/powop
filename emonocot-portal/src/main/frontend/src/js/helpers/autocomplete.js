define([ 'handlebars' ], function(Handlebars) {
  Handlebars.registerHelper('getSuggester', function(key) {
    var suggesterNames = {
      "characteristic" : "Characteristic",
      "common-name" : "Common Name",
      "location" : "Location",
      "scientific-name" : "Name",
    }
    return suggesterNames[key];
  });
});
