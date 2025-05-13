package ch.sofa.lodo.admin.views.gameevents.dialogs;

import ch.sofa.lodo.admin.formatters.NumberConverterUtils;
import ch.sofa.lodo.admin.settings.xml.SettingsXml;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class NewEventDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private Label title;
	private Label message;
	private Button confirm;
	private Button cancel;

	private SettingsXml settingsXml;

	@Autowired
	public NewEventDialog(SettingsXml settingsXml) {
		this.settingsXml = settingsXml;
		createContent();
		createFooter();
		setMessage("You have to pay to create an event.");
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setMessage(String question) {
		this.message.setText(question);
	}

	public void addConfirmationListener(ComponentEventListener<ClickEvent<Button>> listener) {
		confirm.addClickListener(listener);
	}

	public void addCancelClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		cancel.addClickListener(listener);
	}

	private void createContent() {
		message = new Label();

		VerticalLayout content = new VerticalLayout();
		content.add(message);
		content.setPadding(false);
		add(content);
	}

	private void createFooter() {
		cancel = new Button("Cancel", FontAwesome.Solid.TIMES.create());
		cancel.addClickListener(buttonClickEvent -> close());
		String priceFormatted = NumberConverterUtils.getDoublePresentationWithLocale(settingsXml.getEventModule()
				.getNewEventPrice());
		String currency = settingsXml.getEventModule()
				.getNewEventCurrency();
		confirm = new Button("Create for " + priceFormatted + " " + currency, FontAwesome.Solid.CHECK.create());
		confirm.addClickListener(buttonClickEvent -> close());

		HorizontalLayout footer = new HorizontalLayout();
		footer.add(cancel, confirm);
		footer.setJustifyContentMode(JustifyContentMode.EVENLY);
		add(footer);
	}
}
