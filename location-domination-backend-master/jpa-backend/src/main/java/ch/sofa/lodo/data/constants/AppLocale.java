package ch.sofa.lodo.data.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@SuppressWarnings("unused")
public enum AppLocale {

	DE_CH("de", "CH", "ch.sofa.lodo.backend.model.constants.AppLocale.DE_CH"),
	FR_CH("fr", "CH", "ch.sofa.lodo.backend.model.constants.AppLocale.FR_CH"),
	EN_CH("en", "CH", "ch.sofa.lodo.backend.model.constants.AppLocale.EN_CH");

	private static final String LANGUAGE_COUNTRY_SPLITTER = "-";
	private String language;
	private String country;
	private String presentation;

	AppLocale(String language, String country, String presentation) {
		this.language = language;
		this.country = country;
		this.presentation = presentation;
	}

	public static AppLocale getByDbValue(String languageCode) {

		if (StringUtils.isEmpty(languageCode))
			return DE_CH;

		String[] languageCodeSplit = languageCode.split(LANGUAGE_COUNTRY_SPLITTER);
		if (languageCodeSplit.length != 2)
			return DE_CH;

		String tempLanguage = languageCodeSplit[0];
		String tempCountry = languageCodeSplit[1];

		for (AppLocale value : values())
			if (value.country.equals(tempCountry) && value.language.equals(tempLanguage))
				return value;

		return DE_CH;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public String getLocale() {
		return language + LANGUAGE_COUNTRY_SPLITTER + country;
	}

	public String getPresentation() {
		return presentation;
	}

	@Override
	public String toString() {
		return presentation;
	}

	public Locale getUserLocale() {
		return new Locale(language, country);
	}
}
