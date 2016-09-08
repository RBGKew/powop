define([
  'handlebars',
], function(handlebars) {

  handlebars.registerHelper('getSuggester', function(key) {
    var suggesterNames = {"scientific-name" : "Name", "location" : "Location",
    "characteristic" : "Characteristic"}
    return suggesterNames[key];
});

});
