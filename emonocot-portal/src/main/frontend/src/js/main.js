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
        "bootstrap" : "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min",
        "handlebars" : "//cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.runtime",
        "immutable" : "//cdnjs.cloudflare.com/ajax/libs/immutable/3.8.1/immutable.min"
    },

    packages: ["search"]
});

require(['app']);
