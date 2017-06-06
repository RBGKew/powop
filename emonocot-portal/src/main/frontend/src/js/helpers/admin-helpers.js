define(['handlebars', 'libs/lodash'], function(Handlebars, _) {

  function statusClass(status) {
    switch(_.toLower(status)) {
      case 'completed':
        return 'success';
      case 'starting':
      case 'started':
      case 'stopping':
      case 'stopped':
        return 'info';
      case 'failed':
      case 'abandoned':
      case 'unknown':
        return 'danger';
    }

    return 'default';
  }

  Handlebars.registerHelper('panel-status', function(status) {
    return 'panel-' + statusClass(status);
  });

  Handlebars.registerHelper('table-status', function(status) {
    switch(_.toLower(status)) {
      case 'starting':
      case 'started':
      case 'stopping':
      case 'stopped':
        return 'info';
      case 'failed':
      case 'abandoned':
      case 'unknown':
        return 'danger';
    }
  });

  Handlebars.registerHelper('datetime', function(datetime) {
    return new Date(datetime).toLocaleString();
  });
})

