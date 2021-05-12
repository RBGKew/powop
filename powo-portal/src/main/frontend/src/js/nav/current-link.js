define(function(require) {

  var $ = require('jquery');
 
  var setCurrentLink = function() {
    var currentUrl = location.pathname;
    $('.top-right-nav li a').each(function(){
        var $this = $(this);
        if($this.attr('href') === currentUrl ){
          console.log($this.attr('href'))
            $this.addClass('current');
        }
    })
  }

  return {
    setCurrentLink: setCurrentLink
  }
});




