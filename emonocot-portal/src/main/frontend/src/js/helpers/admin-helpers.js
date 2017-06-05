define(['handlebars', 'libs/lodash'], function(Handlebars, _) {
  Handlebars.registerHelper('panel-status', function(status) {
    switch(_.toLower(status)) {
      case 'completed':
        return 'panel-success';
      case 'starting':
      case 'started':
      case 'stopping':
      case 'stopped':
        return 'panel-info';
      case 'failed':
      case 'abandoned':
      case 'unknown':
        return 'panel-danger';
    }

    return 'panel-default';
  });

  Handlebars.registerHelper('datetime', function(datetime) {
    return new Date(datetime).toLocaleString();
  });
})

