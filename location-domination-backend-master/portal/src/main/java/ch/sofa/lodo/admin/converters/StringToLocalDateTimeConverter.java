package ch.sofa.lodo.admin.converters;

import ch.sofa.lodo.data.formatters.DateTimeFormatSupplier;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	private static final long serialVersionUID = 1L;
	private Optional<Locale> locale;

	public StringToLocalDateTimeConverter() {
		this.locale = Optional.empty();
	}

	public StringToLocalDateTimeConverter(Locale locale) {
		this.locale = Optional.ofNullable(locale);
	}

	@Override
	public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
		if (value == null || "".equals(value.trim())) {
			return Result.ok(null);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT)
				.withLocale(locale.orElseGet(() -> context.getLocale()
						.orElseThrow(null)));
		return Result.ok(LocalDateTime.parse(value, formatter));
	}

	@Override
	public String convertToPresentation(LocalDateTime value, ValueContext context) {
		if (value == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT)
				.withLocale(locale.orElseGet(() -> context.getLocale()
						.orElseThrow(null)));
		return formatter.format(value);
	}
}
