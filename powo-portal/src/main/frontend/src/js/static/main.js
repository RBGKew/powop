define(function (require) {
  var search = require("search");
  require("libs/bootstrap");

  return {
    initialize: function () {
      search.initSearch();
    },
  };
});
