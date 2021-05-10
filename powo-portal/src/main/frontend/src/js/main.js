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

    packages: ["search", "taxon", "nav"]
});

require(['app']);
