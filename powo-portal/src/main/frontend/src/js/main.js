require.config({
  baseUrl: "/src/js",
  packages: ["search", "taxon", "static", "nav", "results"],
});

require(["search", "taxon", "static", "nav", "results"]);

console.log(
  "If you want to use POWO data programmatically, please check out our client libraries:\n" +
    "\tPython https://github.com/RBGKew/pykew\n" +
    "\tR https://github.com/barnabywalker/kewr"
);
