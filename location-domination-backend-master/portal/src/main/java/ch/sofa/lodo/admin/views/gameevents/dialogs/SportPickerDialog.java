package ch.sofa.lodo.admin.views.gameevents.dialogs;

import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.services.SportService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SportPickerDialog extends Dialog {

	private static final long serialVersionUID = 1L;
	List<EventSport> eventSports;
	private Label title;
	// private Label question;
	private Button confirm;
	private ComboBox<Sport> sports = new ComboBox<>();
	@Autowired
	private SportService sportService;

	public SportPickerDialog() {
		createContent();
		createFooter();
	}

	public SportPickerDialog(ComponentEventListener<ClickEvent<Button>> listener) {
		this();
		// setTitle(title);
		// setQuestion(content);
		addConfirmationListener(listener);
	}

	@PostConstruct
	public void init() {
		sports.setItemLabelGenerator(e -> e.getName());
	}

	// public void setTitle(String title) {
	// this.title.setText(title);
	// }

	// public void setQuestion(String question) {
	// this.question.setText(question);
	// }

	public void addConfirmationListener(ComponentEventListener<ClickEvent<Button>> listener) {
		confirm.addClickListener(listener);
	}

	private void createContent() {
		// question = new Label();

		VerticalLayout content = new VerticalLayout();
		// content.add(question);
		content.add(sports);
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

	public void setEventSports(List<EventSport> eventSports) {
		this.eventSports = eventSports;
		List<Sport> availableSports = sportService.findAll();
		List<Sport> usetSports = eventSports.stream()
				.map(e -> e.getSport())
				.collect(Collectors.toList());
		List<Sport> notUsedSports = availableSports.stream()
				.filter(e -> !usetSports.contains(e))
				.collect(Collectors.toList());

		sports.setItems(notUsedSports);
	}

	public Sport getSelectedSport() {
		return sports.getValue();
	}
}
