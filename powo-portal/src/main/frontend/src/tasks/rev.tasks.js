module.exports = function (gulp, $) {

  gulp.task('rev', function() {
    return gulp.src([
      'dist/css/style.min.css',
      'dist/js/all.min.js',
      'dist/js/maps/*.min.js.map'
    ], {base: 'dist'})
      .pipe($.rev())
      .pipe(gulp.dest('dist'))
      .pipe($.rev.manifest())
      .pipe(gulp.dest('../resources/'));
  });

}
