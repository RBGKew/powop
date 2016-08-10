/*
* Available tasks
* -----------------
* images:minify - Minify images and output to dist/
* images:sprite - Create SVG & PNG sprite
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
            use: [pngquant()]
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



  /*
	* Create image sprite
	*/
	gulp.task('images:sprite', function() {

		var spritePadding = 3;

		return gulp.src('**/sprite-*.svg', {
          cwd : 'src/img/svg/'
        })
        .pipe($.svgSprite({
          shape : {
            id : {},
            dimension : {
              precision   : 2,
              attributes  : false,
            },
            spacing : {
              padding     : spritePadding,
            },
          },
          mode : {
            /*
            * Standard svg sprite with matching CSS map
            */
            css : {
              dest: 'dist/css/',
              sprite : '../img/sprite/sprite.svg',
              prefix: '.',
              bust : false,
              render: {
                scss: {
                  dest: '../../src/sass/maps/_svg-sprite.scss',
                  template: 'src/sass/maps/_svg-sprite-template.scss',
                }
              }
            },
            /*
            * SVG Symbol defintions to use <use xhref="#"> etc )
            */
            symbol: { // symbol mode to build the SVG
              render: {
                css: false, 
                scss: false 
              },
              dest: 'dist/img/symbol', 
              prefix: '.svg--%s', 
              sprite: 'symbols.svg', 
              example: true 
            }
          },

       

          variables : {
            // Custom variables we use in our svg-sprite-template.scss
            png : function() {
              return function(sprite, render) {
                return render(sprite).split('.svg').join('.png');
              }
            },
            // See this issue https://github.com/jkphl/svg-sprite/issues/134
            // To compensate for your padding value i had to adjust the background-position manually.
            correctPosition: function() {
              return function(backgroundPosition, render) {
                var backgroundPosition = render(backgroundPosition);
                return parseFloat(backgroundPosition - spritePadding)+ "px";
              }
            }
          }
        }))
        .pipe(gulp.dest('.'))
        .pipe($.filter("dist/img/sprite/*.svg"))
        .pipe($.raster())
        .pipe($.rename({extname: '.png'}))
        .pipe(gulp.dest('.'));
  });

};
