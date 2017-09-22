const path = require('path');
const webpack = require('webpack');

module.exports = {
  entry: './app/index.jsx',
  output: {
    path: path.join(__dirname, './resources/js'),
    filename: 'bundle.js',
  },
  module: {
    loaders: [{
      test: /\.(js|jsx)$/,
      exclude: /(node_modules|public)/,
      loader: "babel-loader"
    }, {
      test: /\.json$/,
      loader: 'json-loader'
    }],
  },
  resolve: {
    modulesDirectories: ['node_modules'],
    root: path.resolve('./app'),
    extensions: ['', '.js', '.jsx'],
  },
  plugins: [
    new webpack.NoErrorsPlugin(),
  ],
};
