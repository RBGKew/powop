'use strict';

/*
* Notes and plugins used
* Spliting tasks into seperate files: http://macr.ae/article/splitting-gulpfile-multiple-files.html
* [1] Loading plugins automatically: https://github.com/jackfranklin/gulp-load-plugins
*
*/

/*
* Plugin Requires
*/
var gulp = require('gulp');

/*
* Load all the plugins into the variable $
*/
var $ = require('gulp-load-plugins')({
  pattern: '*',
  rename: {
    'gulp-handlebars': 'ghb'
  }
});

/*
* Pass the plugins along so that your tasks can use them
* Will load all xxx.tasks.js files
*/
$.loadSubtasks('src/tasks/', $);

/*
* Combination tasks
*/
gulp.task('copy', gulp.series('copy:fonts', 'copy:svgs'));
gulp.task('images', gulp.series('images:minify'));
gulp.task('clean', gulp.series('clean:css', 'clean:js', 'clean:templates'));

/*
*  Full build
*/
gulp.task('default', gulp.series(
  'clean',
  'copy',
  'images',
  'js',
  'css',
  'rev'
));

/*
*  Aliases
*/
gulp.task('styles', gulp.series('css', 'rev'));
gulp.task('scripts', gulp.series('precompile', 'js', 'rev'));

/*
* Watch Task
*/
gulp.task('dev', gulp.series('default', function watch() {
  gulp.watch('src/sass/**/*.scss', gulp.series('styles'));
  gulp.watch('src/js/**/*.js', gulp.series('scripts'));
  console.log("Watching 'src/sass' and 'src/js'...");
}));
