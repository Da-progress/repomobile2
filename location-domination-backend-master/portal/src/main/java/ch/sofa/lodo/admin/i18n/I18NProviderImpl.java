package ch.sofa.lodo.admin.i18n;

import ch.sofa.lodo.data.constants.AppLocale;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@SpringComponent
public class I18NProviderImpl implements I18N {

	public static final String BUNDLE_PREFIX = "messages";
	private static final long serialVersionUID = 1L;
	private static final LoadingCache<Locale, ResourceBundle> bundleCache = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(new CacheLoader<Locale, ResourceBundle>() {

				@Override
				public ResourceBundle load(final Locale key) throws Exception {
					return initializeBundle(key);
				}
			});
	public final Locale LOCALE_DE = AppLocale.DE_CH.getUserLocale();
	public final Locale LOCALE_FR = AppLocale.FR_CH.getUserLocale();
	public final Locale LOCALE_EN = AppLocale.EN_CH.getUserLocale();
	private List<Locale> locales = Collections.unmodifiableList(Arrays.asList(LOCALE_DE, LOCALE_FR, LOCALE_EN));

	private static ResourceBundle initializeBundle(final Locale locale) {
		return readProperties(locale);
	}

	protected static ResourceBundle readProperties(final Locale locale) {
		final ClassLoader cl = I18NProviderImpl.class.getClassLoader();

		ResourceBundle propertiesBundle = null;
		try {
			propertiesBundle = ResourceBundle.getBundle("i18n." + BUNDLE_PREFIX, locale, cl);
		} catch (final MissingResourceException e) {
			e.printStackTrace();
		}
		return propertiesBundle;
	}

	@Override
	public List<Locale> getProvidedLocales() {
		return locales;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {

		if (key == null) {
			return "";
		}

		final ResourceBundle bundle = bundleCache.getUnchecked(locale);

		String value = "";

		try {

			value = bundle.getString(key);
			value = new String(value.getBytes(StandardCharsets.ISO_8859_1.name()), StandardCharsets.UTF_8.name());
		} catch (final MissingResourceException e) {
			return "!" + locale.getLanguage() + ": " + key;
		} catch (UnsupportedEncodingException e) {
			return "!" + locale.getLanguage() + ": " + key;
		}

		if (params.length > 0) {
			value = MessageFormat.format(value, params);
		}

		return value;
	}

	@Override
	public String getTranslationWithAppLocale(String key, Object... params) {
		return getTranslation(key, UI.getCurrent()
				.getSession()
				.getLocale(), params);
	}
}
