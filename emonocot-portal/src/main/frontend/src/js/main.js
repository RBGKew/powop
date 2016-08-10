function version($log) {
    window.console && console.log($log);
}
function dbg($log) {
    window.console && console.log($log);
}

require.config({
    baseUrl: '/src/js',

    paths: {
        app: 'app',
    },

    shim : {
        "bootstrap" : { "deps" :['jquery'] }
    },

    paths: {
        "jquery" : "//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min",
        "bootstrap" :  "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min"  
    }
});

require(['app']);
