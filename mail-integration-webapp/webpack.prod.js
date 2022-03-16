const path = require('path');

const config = {
  context: path.resolve(__dirname, '.'),
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: [
          'babel-loader',
          'eslint-loader',
        ]
      },
      {
        test: /\.vue$/,
        use: [
          'vue-loader',
          'eslint-loader',
        ]
      }
    ]
  },
  entry: {
    mailIntegrationNotificationComponent: './src/main/webapp/vue-app/common/main.js',
    mailIntegrationSettings: './src/main/webapp/vue-app/mail-integration-settings/main.js',
    mailIntegrationNotification: './src/main/webapp/vue-app/mail-integration-notification/main.js',
  },
  output: {
    path: path.join(__dirname, 'target/mail-integration/'),
    filename: 'js/[name].bundle.js',
    libraryTarget: 'amd'
  },
  externals: {
    vue: 'Vue',
    vuetify: 'Vuetify',
    jquery: '$',
  },
};

module.exports = config;
