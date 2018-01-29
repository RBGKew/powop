module.exports = function (gulp, $) {

  gulp.task('rev', function() {
    return gulp.src([
      'dist/css/*.min.css',
      'dist/js/*.min.js',
      'dist/js/maps/*.min.js.map'
    ], {base: 'dist'})
      .pipe($.rev())
      .pipe(gulp.dest('dist'))
      .pipe($.rev.manifest())
      .pipe(gulp.dest('../resources/'));
  });

}
