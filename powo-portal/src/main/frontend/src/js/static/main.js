define(function (require) {
  var $ = require("jquery");
  var search = require("search");
  require("libs/bootstrap");

  var initialize = function () {
    // setup search box
    search.initSearch();

    return {
      initialize: initialize,
    };
  };
});
