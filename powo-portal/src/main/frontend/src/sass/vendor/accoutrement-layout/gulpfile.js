/* eslint-env node */

'use strict';

var chalk = require('chalk');
var gulp = require('gulp');
var gutil = require('gulp-util');
var sassdoc = require('sassdoc');
var sasslint = require('gulp-sass-lint');

var paths = {
  TEST_DIR: 'test/',
  SASS_DIR: 'sass/',
  IGNORE: [
    '!**/.#*',
    '!**/flycheck_*'
  ],
  init: function () {
    this.SASS = [
      this.SASS_DIR + '**/*.scss'
    ].concat(this.IGNORE);
    this.ALL_SASS = [
      this.SASS_DIR + '**/*.scss',
      this.TEST_DIR + '**/*.scss'
    ].concat(this.IGNORE);
    return this;
  }
}.init();

var onError = function (err) {
  gutil.log(chalk.red(err.message));
  gutil.beep();
  this.emit('end');
};

var sasslintTask = function (src, failOnError, log) {
  if (log) {
    gutil.log('Running', '\'' + chalk.cyan('sasslint ' + src) + '\'...');
  }
  var stream = gulp.src(src)
    .pipe(sasslint())
    .pipe(sasslint.format())
    .pipe(sasslint.failOnError());
  if (!failOnError) {
    stream.on('error', onError);
  }
  return stream;
};

gulp.task('sasslint', function () {
  return sasslintTask(paths.ALL_SASS, true);
});

gulp.task('sasslint-nofail', function () {
  return sasslintTask(paths.ALL_SASS);
});

gulp.task('sassdoc', function () {
  return gulp.src(paths.SASS)
    .pipe(sassdoc());
});

gulp.task('watch', function () {
  gulp.watch(paths.SASS, ['sassdoc']);

  // lint scss on changes
  gulp.watch(paths.ALL_SASS, function (ev) {
    if (ev.type === 'added' || ev.type === 'changed') {
      sasslintTask(ev.path, false, true);
    }
  });

  // lint all scss when rules change
  gulp.watch('**/.sass-lint.yml', ['sasslint-nofail']);
});
