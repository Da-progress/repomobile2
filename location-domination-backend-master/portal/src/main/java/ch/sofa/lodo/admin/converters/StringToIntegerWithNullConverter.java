package ch.sofa.lodo.admin.converters;

import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

public class StringToIntegerWithNullConverter extends StringToIntegerConverter {

	private static final long serialVersionUID = 1L;

	public StringToIntegerWithNullConverter(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String convertToPresentation(Integer value, ValueContext context) {
		if (value == null) {
			return "";
		}

		return getFormat(context.getLocale().orElse(null)).format(value);
	}
}
