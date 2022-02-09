require.config({
  baseUrl: "/src/js",
  packages: ["search", "taxon", "static", "nav", "results"],
});

require(["search", "taxon", "static", "nav", "results"]);
