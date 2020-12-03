/*
* Available tasks
* -----------------
* images:minify - Minify images and output to dist/
*/
module.exports = function (gulp, $) {

	var pngquant = require('imagemin-pngquant');
	
	/*
  * Minify images
  */ 
  gulp.task('images:minify', function() {

    const rasterFilter = $.filter('src/img/raster/*.{png,gif,jpg,jpeg}', {restore: true});
    const svgFilter = $.filter('src/img/svg/*.svg', {restore: true});

		return gulp.src(['src/img/raster/*.{png,gif,jpg,jpeg}', 'src/img/svg/*.svg'])
      .pipe(rasterFilter)
      .pipe($.imagemin({
            progressive: true,
            svgoPlugins: [{removeViewBox: false}],
            use: [$.imagemin.optipng({optimizationLevel: 5})]
        }))
      .pipe(gulp.dest('dist/img/raster'))
      .pipe(rasterFilter.restore)
      .pipe(svgFilter)
      .pipe($.raster())
      .pipe($.rename({extname: '.png'}))
      .pipe($.imagemin({
            progressive: true,
            svgoPlugins: [{removeViewBox: false}],
            use: [pngquant()]
        }))
      .pipe(gulp.dest('dist/img/raster'))
  });
};
