package ch.sofa.lodo.admin.utils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExceptionUtils {

	private ExceptionUtils() {
	}

	public static List<String> getExceptionMessageChain(Throwable throwable) {

		List<String> result = new ArrayList<String>();
		while (throwable != null) {
			if (NullPointerException.class.isAssignableFrom(throwable.getClass())) {
				result.add(NullPointerException.class.getSimpleName());
			} else if (ConstraintViolationException.class.isAssignableFrom(throwable.getClass())) {

				Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) throwable).getConstraintViolations();
				if (constraintViolations != null) {
					for (ConstraintViolation<?> violation : constraintViolations) {
						result.add(violation.getMessage());
					}
				}
			} else {
				result.add(throwable.getMessage());
			}
			throwable = throwable.getCause();
		}

		return result;
	}

	public static String getExceptionMessages(Throwable throwable) {
		return getOneExceptionMessageString(getExceptionMessageChain(throwable), "<br>");
	}

	public static String getExceptionMessages(Throwable throwable, String linebreak) {
		return getOneExceptionMessageString(getExceptionMessageChain(throwable), linebreak);
	}

	public static String getOneExceptionMessageString(List<String> exceptionMessageChain, String linebreak) {

		StringBuilder sb = new StringBuilder();
		for (String message : exceptionMessageChain) {
			sb.append(linebreak)
					.append(message);
		}
		sb.replace(0, linebreak.length(), "");

		return sb.toString();
	}

	public static String getStringFromException(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
