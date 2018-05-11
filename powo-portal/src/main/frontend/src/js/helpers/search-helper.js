define(['handlebars', 'libs/lodash'], function(Handlebars, _) {
  Handlebars.registerHelper('selected', function(variable, value) {
    return (
      _.isString(variable) && variable.includes(value)
      || variable === value
    ) ? 'selected' : '';
  });
});
