package ch.sofa.lodo.admin.formatters;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberConverterUtils {

	private static NumberFormat numberInstance;

	public static NumberFormat getNumberFormat() {
		if (numberInstance == null) {
			numberInstance = NumberFormat.getNumberInstance(new Locale("de", "CH"));
			numberInstance.setMinimumFractionDigits(2);
			numberInstance.setMaximumFractionDigits(2);
			numberInstance.setRoundingMode(RoundingMode.HALF_UP);
			numberInstance.setGroupingUsed(true);
		}

		return numberInstance;
	}

	public static String getDoublePresentationWithLocale(Double value) {
		if (value == null) {
			return getNumberFormat().format(Double.valueOf(0.0));
		}

		return getNumberFormat().format(value);
	}

	public static Double parseWithLocale(String value) throws ParseException {
		Number num = getNumberFormat().parse(value);
		if (num instanceof Long) {
			return num.doubleValue();
		} else if (num instanceof Integer) {
			return num.doubleValue();
		}
		return (Double) num;
	}
}
