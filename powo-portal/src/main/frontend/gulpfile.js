'use strict';

/*
* Notes and plugins used
* Spliting tasks into seperate files: http://macr.ae/article/splitting-gulpfile-multiple-files.html
* [1] Loading plugins automatically: https://github.com/jackfranklin/gulp-load-plugins
*
*/

/**
 * Command Line Arguments
 */
var argv = require('yargs').argv;

/*
* Plugin Requires
*/
var gulp = require('gulp');
var browserSync = require('browser-sync');

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
gulp.task('images', gulp.series('images:minify', "images:icons"));
gulp.task('clean', gulp.series('clean:css', 'clean:js', 'clean:templates', 'clean:images'));

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
gulp.task('scripts', gulp.series('js', 'rev'));

/*
* Watch Task
*/
const PORT = argv.port || 3000
const BACKEND_PORT = argv.backendPort || 10080
gulp.task('browsersync', function() {
  browserSync.init({
    port: PORT,
    proxy: "localhost:" + BACKEND_PORT,
    logLevel: "debug"
  });
})

const reload = (done) => {
  browserSync.reload()
  done()
}

gulp.task('watch', gulp.parallel('browsersync', function() {
  gulp.watch('src/sass/**/*.scss', gulp.series('css', reload));
  gulp.watch([
    'src/js/**/*.js',
    'src/templates/**/*.hbs',
    '!src/js/templates/**/*.js'
  ], gulp.series('js', reload));
  console.log("Watching 'src/sass', 'src/js' and 'src/templates'...");
}));

/*
* Dev Task
*/
gulp.task('dev', gulp.series('default', 'watch'));
