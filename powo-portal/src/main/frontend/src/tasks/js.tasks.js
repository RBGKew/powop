/*
 * Available tasks
 * -----------------
 * js - Run requirejs to create a single all.min.js file
 */
var config = {
  requireConfig: {
    baseUrl: "src/js/",
    name: "main",
    out: "all.js",
    include: ["libs/require", "main"],
    mainConfigFile: "src/js/main.js",
  },
};

module.exports = function (gulp, $) {
  gulp.task("precompile", function () {
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
  });

  gulp.task(
    "js:dev",
    gulp.series("precompile", function compile() {
      return $.requirejs(config.requireConfig)
        .pipe($.sourcemaps.init({ loadMaps: true }))
        .pipe($.rename({ extname: ".min.js" }))
        .pipe($.sourcemaps.write("maps", { sourceMappingURLPrefix: "/js" }))
        .pipe(gulp.dest("dist/js"));
    })
  );

  gulp.task(
    "js",
    gulp.series("precompile", function compile() {
      return $.requirejs(config.requireConfig)
        .pipe($.sourcemaps.init({ loadMaps: true }))
        .pipe($.uglify())
        .pipe($.rename({ extname: ".min.js" }))
        .pipe($.sourcemaps.write("maps", { sourceMappingURLPrefix: "/js" }))
        .pipe(gulp.dest("dist/js"));
    })
  );
};
