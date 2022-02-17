/*
 * Available tasks
 * -----------------
 * js - Run requirejs to create a single all.min.js file
 */

/**
 * We have to use a function here because the object is modified in place by the RequireJS plugin
 */
function requireJsConfig() {
  return {
    baseUrl: "src/js/",
    name: "main",
    out: "all.js",
    include: ["libs/require", "main"],
    mainConfigFile: "src/js/main.js",
  };
}

module.exports = function (gulp, $) {
  function precompile() {
    // precompile handlebars templates for use in frontend
    return gulp
      .src("src/templates/**/*.hbs")
      .pipe(
        $.ghb({
          handlebars: require("handlebars"),
        })
      )
      .pipe($.defineModule("amd"))
      .pipe(gulp.dest("src/js/templates/"));
  }

  function compileDev() {
    return $.requirejs(requireJsConfig())
      .on("error", e => console.error(e))
      .pipe($.sourcemaps.init({ loadMaps: true }))
      .pipe($.rename({ extname: ".min.js" }))
      .pipe($.sourcemaps.write("maps", { sourceMappingURLPrefix: "/js" }))
      .pipe(gulp.dest("dist/js"));
  }

  function compile() {
    return $.requirejs(requireJsConfig())
      .pipe($.sourcemaps.init({ loadMaps: true }))
      .pipe($.uglify())
      .pipe($.rename({ extname: ".min.js" }))
      .pipe($.sourcemaps.write("maps", { sourceMappingURLPrefix: "/js" }))
      .pipe(gulp.dest("dist/js"));
  }

  gulp.task("js:dev", gulp.series(precompile, compileDev));
  gulp.task("js", gulp.series(precompile, compile));
};
