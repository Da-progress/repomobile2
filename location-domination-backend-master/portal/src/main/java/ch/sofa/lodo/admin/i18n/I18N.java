package ch.sofa.lodo.admin.i18n;

import com.vaadin.flow.i18n.I18NProvider;

public interface I18N extends I18NProvider {

	String getTranslationWithAppLocale(String key, Object... params);
}