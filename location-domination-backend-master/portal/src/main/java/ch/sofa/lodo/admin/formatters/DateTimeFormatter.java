package ch.sofa.lodo.admin.formatters;

import ch.sofa.lodo.admin.converters.TimeZoneConverter;
import ch.sofa.lodo.data.formatters.DateTimeFormatSupplier;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@VaadinSessionScope
@SpringComponent
public class DateTimeFormatter {

	private TimeZoneConverter timeZoneConverter;

	@Autowired
	public DateTimeFormatter(TimeZoneConverter timeZoneConverter) {
		this.timeZoneConverter = timeZoneConverter;
	}

	public String formatDateTimeNoSec(LocalDateTime date, String sourceTimeZoneId, String targetTimeZoneId) {
		if (date == null) {
			return "";
		}
		LocalDateTime target = timeZoneConverter.convert(date, sourceTimeZoneId, targetTimeZoneId);
		return format(target);
	}

	/**
	 * Convert from source ZoneId (UTC as default) to browser ZoneId
	 *
	 * @param date
	 * @return formated date-time string
	 */
	public String formatDateTimeNoSec(LocalDateTime date) {
		LocalDateTime target = timeZoneConverter.convertToTarget(date);
		return format(target);
	}

	private String format(LocalDateTime target) {
		return target == null ? ""
				: target.format(
				java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT_NO_SEC));
	}
}
