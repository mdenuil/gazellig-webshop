// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html
const os = require('os');
const chromeHeadlessSupported = os.platform() !== 'win32' || Number((os.release().match(/^(\d+)/) || ['0', '0'])[1]) >= 10;

module.exports = function (config) {
	config.set({
		basePath: '',
		frameworks: ['jasmine', '@angular-devkit/build-angular', 'karma-typescript'],
		preprocessors: {
			'**/*.spec.ts': ['karma-typescript']
		},
		plugins: [
			require('karma-jasmine'),
			require('karma-typescript'),
			require('karma-chrome-launcher'),
			require('karma-jasmine-html-reporter'),
			require('karma-coverage-istanbul-reporter'),
			require('@angular-devkit/build-angular/plugins/karma')
		],
		client: {
			clearContext: false // leave Jasmine Spec Runner output visible in browser
		},
		coverageIstanbulReporter: {
			dir: require('path').join(__dirname, './coverage'),
			reports: ['html', 'lcovonly'],
			fixWebpackSourcePaths: true
		},
		reporters: ['progress', 'kjhtml', 'karma-typescript'],
		port: 9876,
		colors: true,
		logLevel: config.LOG_INFO,
		autoWatch: true,
		browsers: [
			chromeHeadlessSupported ? 'ChromeHeadless' : 'Chrome'
		],
		customLaunchers: {
			ChromeHeadless: {
				base: 'Chrome',
				flags: ['--no-sandbox', '--headless', '--disable-gpu', '--remote-debugging-port=9222']
			}
		},
		singleRun: true
	});
};