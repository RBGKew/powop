/*
* Available tasks
* -----------------
* copy:fonts  - Copy `src/fonts` to 'dist/fonts'
* copy:js  - Copy non AMD js from `src/js` to 'dist/js'
* copy:svg  - Copy `src/img/svg` to 'dist/img/svg'
* copy:videos  - Copy `src/videos` to 'dist/videos'
*/
module.exports = function (gulp, $) {

	/*
	* Copy fonts
	*/
	gulp.task('copy:fonts', function() {
	 return gulp.src( 'src/fonts/**/*.{ttf,woff,woff2,eot,svg}')
       .pipe(gulp.dest('dist/fonts/'));

  });

	/*
  * Copy js
  */ 
  gulp.task('copy:js', function() {
		return gulp.src(['src/js/libs/modernizr.custom.js'])
      .pipe(gulp.dest('dist/js'));
  });

  /*
  * Copy svg
  */ 
  gulp.task('copy:svgs', function() {
		return gulp.src('src/img/svg/**/*.svg')
      .pipe(gulp.dest('dist/img/svg'));
  });

  /*
  * Copy videos
  */ 
  gulp.task('copy:videos',  function() {
		return gulp.src('src/videos/**/*.{mp4, webm, ogg}')
       .pipe(gulp.dest('dist/videos'));
  });
};
