import { browser, by, element } from 'protractor';

export class AppPage {
	navigateTo() {
		return browser.get(browser.baseUrl) as Promise<any>;
	}

	getTitleText() {
		return element(by.css('app-root #main app-navigation mat-toolbar a h1')).getText() as Promise<
			string
		>;
	}

	getProducts() {
		return element(by.css('app-root #content app-artikel-overzicht main section')).all(by.css('app-artikel')).count() as Promise<
			number
		>;
	}
}
