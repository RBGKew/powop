/*
 * Available tasks
 * -----------------
 * js - Run requirejs to create a sinle all.min.js file
 */
module.exports = function (gulp, $) {

  var defineModule = require('gulp-define-module');
  var handlebars = require('gulp-handlebars');
  var rjs = require('gulp-requirejs');

  gulp.task('precompile', function() {
    // precompile handlebars templates for use in frontend
    return gulp.src('src/templates/**/*.hbs')
      .pipe(handlebars({
        handlebars: require('handlebars')
      }))
      .pipe(defineModule('amd'))
      .pipe(gulp.dest('src/js/templates/'));
  });

  gulp.task('js', ['precompile'], function() {
    return rjs({
      baseUrl: 'src/js/',
      name: 'main',
      out: 'all.js',
      include: ['libs/require', 'main'],
      mainConfigFile: 'src/js/' + 'main.js',
      shim: {
        // standard require.js shim options
      }
    })
      .pipe(gulp.dest('dist/js'))
      .pipe($.uglify())
      .pipe($.rename('all.min.js'))
      .pipe(gulp.dest('dist/js'));
  });
};
