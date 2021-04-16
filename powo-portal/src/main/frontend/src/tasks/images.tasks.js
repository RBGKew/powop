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
  gulp.task('images:minify:raster', function() {
    return gulp.src('src/img/raster/**/*.{png,gif,jpg,jpeg}')
      .pipe($.imagemin({
            progressive: true,
            use: [$.imagemin.optipng({optimizationLevel: 5})]
        }))
      .pipe(gulp.dest('dist/img/raster'))
  })

  gulp.task('images:minify:svg', function() {
    return gulp.src('src/img/svg/*.svg')
      .pipe($.raster())
      .pipe($.rename({extname: '.png'}))
      .pipe($.imagemin({
            progressive: true,
            svgoPlugins: [{removeViewBox: false}],
            use: [pngquant()]
        }))
      .pipe(gulp.dest('dist/img/raster'))
  })

  gulp.task('images:minify', gulp.series('images:minify:raster', 'images:minify:svg'));

  gulp.task("images:icons", function () {
    return gulp.src(["src/img/*.ico"]).pipe(gulp.dest("dist/img"));
  });
};
