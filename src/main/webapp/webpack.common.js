/* eslint-disable */
const path = require('path')
const webpack = require('webpack')
const NyanProgressPlugin = require('nyan-progress-webpack-plugin')
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

module.exports = {
  entry: {
    app: './src/index.js',
    vendor: [
      'babel-polyfill',
      'dva',
      'react',
      'react-dom',
      'highlight.js',
      'codemirror',
      'lodash',
    ]
  },
  output: {
    filename: 'app.js',
    path: path.resolve(__dirname, 'resources', 'js'),
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: 'babel-loader',
      },
      {
        test: /\.css$/,
        use: [
          'style-loader',
          'css-loader',
        ]
      },
      {
        test: /\.json$/,
        use: 'json-loader',
      },
    ],
  },
  resolve: {
    modules: [__dirname, 'node_modules'],
    extensions: ['*', '.js', '.jsx'],
  },
  plugins: [
    new NyanProgressPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      filename: 'vendor.js',
    }),
    // new BundleAnalyzerPlugin({
      // analyzerMode: 'static',
      // reportFilename: path.resolve(__dirname, 'report.html'),
      // openAnalyzer: false,
    // }),
  ],
}
