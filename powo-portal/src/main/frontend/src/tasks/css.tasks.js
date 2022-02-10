/*
 * Available tasks
 * -----------------
 * css - Run sass and create the css files via autoprefixer and minification
 */
module.exports = function (gulp, $) {
  gulp.task("lint", function () {
    return gulp
      .src("src/sass/**/*.s+(a|c)ss")
      .pipe(
        $.sassLint({
          options: {
            formatter: "stylish",
          },
          configFile: "sass-lint.yml",
        })
      )
      .pipe($.sassLint.format());
  });

  gulp.task("css:dev", function () {
    return gulp
      .src(["src/sass/style.scss"])
      .pipe($.sourcemaps.init())
      .pipe($.gsass().on("error", $.gsass.logError))
      .pipe($.rename({ extname: ".min.css" }))
      .pipe($.sourcemaps.write())
      .pipe(gulp.dest("dist/css"));
  });

  gulp.task("css", function () {
    return gulp
      .src(["src/sass/style.scss"])
      .pipe($.gsass().on("error", $.gsass.logError))
      .pipe(
        $.autoprefixer({
          browsers: ["last 2 versions"],
          cascade: false,
        })
      )
      .pipe(
        $.cssnano({
          safe: true,
          autoprefixer: false,
        })
      )
      .pipe($.rename({ extname: ".min.css" }))
      .pipe(gulp.dest("dist/css"));
  });
};
