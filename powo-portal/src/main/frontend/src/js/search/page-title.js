define(function () {
  function updatePageTitle(siteData, filterData) {
    if (filterData.length > 0) {
      var title = filterData
        .map(function (f) {
          return f.value;
        })
        .join(", ");

      document.title = title + " | " + siteData.basePageTitle;
    } else {
      document.title = siteData.basePageTitle;
    }
  }
  return {
    updatePageTitle: updatePageTitle,
  };
});
