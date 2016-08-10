/*
* Available tasks
* -----------------
* html - Run assemble
*/
module.exports = function (gulp, $, browserSync) {

  var app = $.assemble();

  gulp.task('assemble:load', function(cb) {
    app.partials('src/templates/partials/**/*.hbs');
    app.layouts('src/templates/layouts/*.hbs');
    app.pages('src/templates/pages/*.hbs');
    cb();
  });

  /*
  * html - run assemble
  */ 
  gulp.task('assemble', ['assemble:load'], function() {
    return app.toStream('pages')
      .pipe(app.renderFile())
      .pipe($.htmlmin())
      .pipe(gulp.dest('dist/templates/'))
      .pipe(browserSync.stream());
  });
};
