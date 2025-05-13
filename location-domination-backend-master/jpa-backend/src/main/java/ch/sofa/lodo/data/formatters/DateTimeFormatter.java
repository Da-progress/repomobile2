package ch.sofa.lodo.data.formatters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Used on backend only.
 */
public class DateTimeFormatter {

	public static String formatDate(LocalDate date) {
		return date == null ? "" : date.format(java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_FORMAT));
	}

	public static LocalDate parse(String date) {
		if (date == null) {
			return null;
		}
		return LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_FORMAT));
	}

	public static String formatDateTime(LocalDateTime date) {
		return date == null ? "" : date.format(java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT));
	}

	public static String formatDateTimeNoSec(LocalDateTime date) {
		return date == null ? "" : date.format(java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT_NO_SEC));
	}

	public static String formatDateTimeNoSec(LocalDateTime date, String sourceTimeZoneId, String targetTimeZoneId) {
		if (date == null) {
			return "";
		}
		ZonedDateTime zonedSourceDate = date.atZone(ZoneId.of(sourceTimeZoneId));
		ZonedDateTime zonedTargetDate = zonedSourceDate.withZoneSameInstant(ZoneId.of(targetTimeZoneId));
		return zonedTargetDate.format(java.time.format.DateTimeFormatter.ofPattern(DateTimeFormatSupplier.DATE_TIME_FORMAT_NO_SEC));
	}
}
