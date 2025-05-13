package ch.sofa.lodo.admin.session;

import com.vaadin.flow.component.page.ExtendedClientDetails;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@VaadinSessionScope
@SpringComponent
public class ClientBrowserData {

	private ExtendedClientDetails details;

	public ExtendedClientDetails getDetails() {
		return details;
	}

	public void setDetails(ExtendedClientDetails details) {
		this.details = details;
	}
}
