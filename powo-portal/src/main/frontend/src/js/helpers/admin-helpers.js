define(['handlebars', 'libs/lodash'], function(Handlebars, _) {

  function pickStatusFromExecution(execution) {
    if(_.toLower(execution.status) === 'completed') {
      return execution.exitStatus.exitCode;
    } else {
      return execution.status;
    }
  }

  function pickStatus(status, exitCode) {
    return pickStatusFromExecution({
      status: status,
      exitStatus: {exitCode: exitCode}
    });
  }

  Handlebars.registerHelper('job-status', function(execution) {
    return pickStatusFromExecution(execution);
  });

  Handlebars.registerHelper('job-status', function(status, exitCode) {
    return pickStatus(status, exitCode);
  });

  Handlebars.registerHelper('panel-status', function(status, exitCode) {
    switch(_.toLower(pickStatus(status, exitCode))) {
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

  Handlebars.registerHelper('table-status', function(execution) {
    switch(_.toLower(pickStatusFromExecution(execution))) {
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

