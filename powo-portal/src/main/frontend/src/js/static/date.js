define(function(require) {

  var moment = require('libs/moment')

  var fullYear = function() {
    var fullYear = moment().format("YYYY");
    return fullYear
  }
 
  var fullDate = function() {
    var fullDate = moment().format("DD MMMM YYYY");
    return fullDate
  }

  return {
    fullYear: fullYear,
    fullDate: fullDate
  }
});




