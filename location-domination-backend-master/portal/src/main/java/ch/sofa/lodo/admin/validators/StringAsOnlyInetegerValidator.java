package ch.sofa.lodo.admin.validators;

import com.vaadin.flow.data.validator.RegexpValidator;

public class StringAsOnlyInetegerValidator extends RegexpValidator {

	private static final long serialVersionUID = 1L;
	private static final String PATTERN = "^" + "[0-9]*";

	public StringAsOnlyInetegerValidator(String errorMessage) {
		super(errorMessage, PATTERN, true);
		// TODO Auto-generated constructor stub
	}
}
