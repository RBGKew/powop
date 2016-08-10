/*
* Available tasks
* -----------------
* js - Run requirejs to create a sinle all.min.js file
*/
module.exports = function (gulp, $) {

  var rjs = require('gulp-requirejs');

  gulp.task('js', function() {

      rjs({
        baseUrl: 'src/js/',
        name: 'main',
        out: 'all.js',
        include: ['libs/require', 'main'],
        mainConfigFile: 'src/js/' + 'main.js',
        shim: {
                // standard require.js shim options 
        },
      })
      .pipe(gulp.dest('dist/js'))
      .pipe($.uglify())
      .pipe($.rename('all.min.js'))
      .pipe(gulp.dest('dist/js'));
 
  });

};
