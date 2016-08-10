define(['jquery', 'ieCheck'], function($, ieCheck) {

	return { 

    init: function() {
      var ieVersion = ieCheck.getVersion();

      if( ieVersion && ieVersion < 9 ) {
        this.replaceSVG();
      };
    },


    replaceSVG: function() {
      /*
      * Replace any inline svgs with class '.svg' using the 
      * With the png fallback
      */
      var $svgs = $("svg.svg");

      $svgs.each(function(index, value){
        var classes = $(value).attr('class'),
            imageName = $(value).children().attr('xlink:href').split('#')[1],
            $image = $("<div class='" + classes + "'><img src='/_assets/img/svg2png/" + imageName + ".png'></div>");
        $(value).after($image).remove();
      })
    }
  }
});
