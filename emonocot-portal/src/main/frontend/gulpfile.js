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
var browserSync = require('browser-sync').create();

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
$.loadSubtasks('src/tasks/', $, browserSync);

/*
* Static browser sync server
*/
gulp.task('browser-sync', function() {
    browserSync.init({
        server: {
            baseDir: "./dist"
        }
    });
    gulp.watch('src/sass/**/*.scss',['css']);
    gulp.watch('src/js/search/**/*.js',['precompile', 'js']);
});

/*
* Combination tasks
*/
gulp.task('copy', ['copy:fonts', 'copy:svgs']);
gulp.task('clean', ['clean:css', 'clean:js', 'clean:templates']);

/*
* Watch Task
*/
gulp.task('dev', function() {
  gulp.watch('src/sass/**/*.scss',['css']);
  gulp.watch('src/js/**/*.js',['scripts']);
});

/*
*  Full build
*/
gulp.task('default', function(cb) {
  $.runSequence(
    'clean',
    'copy',
    'js',
    'css',
    'rev',
    cb);
});

/*
*  Aliases
*/
gulp.task('styles', function(cb) {
  $.runSequence('css', 'rev', cb)
});
gulp.task('scripts', function(cb) {
  $.runSequence('precompile', 'js', 'rev', cb)
});
gulp.task('serve', ['browser-sync']);
