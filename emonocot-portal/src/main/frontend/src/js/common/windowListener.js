define(['jquery'], function($) {

	return { 

    breakpoint:null,

    init: function(){
      this.setBreakpoint();
      this.resizeUpdate();
    },

    setBreakpoint: function(){
      /*
      * Read the CSS value of the body:before to get the breakpoint
      */
      this.breakpoint = window.getComputedStyle(document.querySelector('body'), ':before').getPropertyValue('content').replace(/\"/g, '');
    },

    getBreakpoint: function() {
      return this.breakpoint;
    },

    resizeUpdate: function() {
      /*
      * Update the breakpoint when the user resizes
      */
        var resizeListener = $.proxy(function(e) { 
        $(window).one("resize", $.proxy(function(e) { 
          this.setBreakpoint();
          console.log(this.breakpoint);
          setTimeout(resizeListener,100); 
          }, this));
        }, this);
      resizeListener();
    }
 
	}
});
