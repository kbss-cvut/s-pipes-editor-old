var path = require('path');
var webpack = require('webpack');
var production = process.env.NODE_ENV === 'production' || false;
module.exports = {
    entry: './js/app.js',
    output: {
        filename: production ? 'clipboard.min.js' : 'clipboard.js',
        path: path.resolve(__dirname, 'dist'),
        library: 'Clipboard',
        libraryTarget: 'umd'
    },
    module: {
        rules: [
            {
                test: /\.js$/, exclude: /node_modules/, loader: "babel-loader", query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    plugins: production ? [
        new webpack.optimize.UglifyJsPlugin({
            beautify: false,
            mangle: {
                screw_ie8: true,
                keep_fnames: true
            },
            compress: {
                screw_ie8: true
            },
            comments: false
        })
    ] : []
};