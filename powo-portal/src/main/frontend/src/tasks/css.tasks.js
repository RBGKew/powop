/*
* Available tasks
* -----------------
* css - Run sass and create the css files via autoprefixer and minification
*/
module.exports = function (gulp, $, browserSync) {

  gulp.task('lint', function() {
    return gulp.src('src/sass/**/*.s+(a|c)ss')
      .pipe($.sassLint({
        options: {
          formatter: 'stylish'
        },
        configFile: 'sass-lint.yml'
      }))
      .pipe($.sassLint.format())
  });

  gulp.task('css', function() {

    return gulp.src(['src/sass/style.scss'])
      .pipe($.sass()
            .on('error', $.sass.logError)
      )
      .pipe($.autoprefixer({
        browsers: ['last 2 versions'],
        cascade: false
      }))
      .pipe(gulp.dest('dist/css'))
      .pipe($.cssnano({
        'safe':true,
        'autoprefixer': false
      }))
      .pipe(browserSync.stream())
      .pipe($.rename({extname: '.min.css'}))
      .pipe(gulp.dest('dist/css'));
    
    });
   
};
