require.config({
  baseUrl: "/src/js",
  packages: ["search", "taxon", "static", "nav", "results"],
});

require(["search", "taxon", "static", "nav", "results"]);

console.log(
  "If you want to use POWO data programmatically, please check out our client libraries:" +
    "\tPython\thttps://github.com/RBGKew/pykew" +
    "\tR\t\thttps://github.com/barnabywalker/kewr"
);
