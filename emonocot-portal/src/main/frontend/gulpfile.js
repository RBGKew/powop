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
gulp.task('copy', ['copy:fonts', 'copy:svgs', 'copy:videos', 'copy:js']);
gulp.task('images', ['images:sprite', 'images:minify', 'copy:svgs']);
gulp.task('clean', ['clean:dist', 'clean:templates']);

/*
* Watch Task
*/
gulp.task('dev', function() {
  gulp.watch('src/sass/**/*.scss',['css']);
  gulp.watch('src/js/**/*.js',['precompile', 'js']);
});

/*
*  Full build
*/
gulp.task('default', function(cb){
  $.runSequence('clean', 'images',  [
    'copy',
    'precompile',
    'js',
    'css',
  ], cb);
});

/*
*  Aliases
*/
gulp.task('styles', ['css']);
gulp.task('scripts', ['precompile', 'js']);
gulp.task('serve', ['browser-sync']);
