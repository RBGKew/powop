define(['jquery'], function($) {

	return { 

        $html: null,
        ieVersion: null,

        getVersion: function() {
            /* 
            * IE version detection based on html classes 
            */
            this.$html = $("html");
                
            if ( this.$html.hasClass("lt-ie8") ) {
                this.ieVersion = 7;
            } else if ( this.$html.hasClass("lt-ie9") ) {
                this.ieVersion = 8;
            } else if ( this.$html.hasClass("lt-ie10") ) {
                this.ieVersion = 9;
            }

            return this.ieVersion; 

        }
	}
});
