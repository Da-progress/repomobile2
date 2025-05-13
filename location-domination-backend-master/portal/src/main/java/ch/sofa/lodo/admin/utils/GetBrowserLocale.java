package ch.sofa.lodo.admin.utils;

import ch.sofa.lodo.data.constants.AppLocale;
import com.vaadin.flow.server.WebBrowser;

import java.util.Locale;

public class GetBrowserLocale {

	public static Locale getBrowserLocale(WebBrowser browser) {

		Locale locale = browser.getLocale();

		if (locale != null) {

			if (locale.getLanguage()
					.equalsIgnoreCase(AppLocale.DE_CH.getLanguage())) {
				locale = AppLocale.DE_CH.getUserLocale();
			} else if (locale.getLanguage()
					.equalsIgnoreCase(AppLocale.FR_CH.getLanguage())) {
				locale = AppLocale.FR_CH.getUserLocale();
			} else {
				locale = AppLocale.EN_CH.getUserLocale();
			}
		} else {

			locale = AppLocale.EN_CH.getUserLocale();
		}

		return locale;
	}
}
