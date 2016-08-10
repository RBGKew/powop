/*
* Available tasks
* -----------------
* clean:dist - Clean the 'dist' folder
* clean:images - Clean just the 'dist/img folder
*/
module.exports = function (gulp, $) {

	/*
  * Clean 'dist/img' folder only
  */ 
  gulp.task('clean:images', function() {
		return $.del(["dist/img/*"], { force: true });
  });


  /*
	* Clean all
	*/
	gulp.task('clean:dist', function() {
		return $.del(["dist/**/*"], { force: true });
  });

};
