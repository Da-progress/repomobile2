package ch.sofa.lodo.admin.components;

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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class ConfirmationDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private Label title;
	private Label question;
	private Button confirm;

	public ConfirmationDialog() {
		createContent();
		createFooter();
	}

	public ConfirmationDialog(String title, String content, ComponentEventListener<ClickEvent<Button>> listener) {
		this();
		setTitle(title);
		setQuestion(content);
		addConfirmationListener(listener);
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setQuestion(String question) {
		this.question.setText(question);
	}

	public void addConfirmationListener(ComponentEventListener<ClickEvent<Button>> listener) {
		confirm.addClickListener(listener);
	}

	private void createContent() {
		question = new Label();

		VerticalLayout content = new VerticalLayout();
		content.add(question);
		content.setPadding(false);
		add(content);
	}

	private void createFooter() {
		Button abort = new Button("No", FontAwesome.Solid.TIMES.create());
		abort.addClickListener(buttonClickEvent -> close());
		confirm = new Button("Yes", FontAwesome.Solid.CHECK.create());
		confirm.addClickListener(buttonClickEvent -> close());

		HorizontalLayout footer = new HorizontalLayout();
		footer.add(abort, confirm);
		footer.setJustifyContentMode(JustifyContentMode.EVENLY);
		add(footer);
	}
}