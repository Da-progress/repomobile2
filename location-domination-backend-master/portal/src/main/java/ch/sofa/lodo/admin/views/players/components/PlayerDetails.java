package ch.sofa.lodo.admin.views.players.components;

import ch.sofa.lodo.admin.converters.StringToLocalDateTimeConverter;
import ch.sofa.lodo.admin.converters.TimeZoneConverter;
import ch.sofa.lodo.admin.notifications.Notifications;
import ch.sofa.lodo.admin.views.players.events.PlayersGridSelectionEvent;
import ch.sofa.lodo.admin.views.players.events.RefreshPlayersGridEvent;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.PlayerService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Locale;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class PlayerDetails extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;

	private FormLayout form;
	private HorizontalLayout commandLayout;

	// private User instance = new User();
	private Binder<User> binder = new Binder<>();

	private TextField username = new TextField();
	private TextField mobileNumber = new TextField();
	private TextField registerDate = new TextField();
	private Checkbox authenticated = new Checkbox();
	private Checkbox blocked = new Checkbox();
	private TextField authCode = new TextField();

	private Button saveButton = new Button(FontAwesome.Solid.SAVE.create());

	private User currentUser;

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private TimeZoneConverter timeZoneConverter;

	private Locale locale;

	private User instance = new User();
	;

	private void enableFormFields(boolean enabled) {
		username.setEnabled(enabled);
		mobileNumber.setEnabled(enabled);
		registerDate.setEnabled(enabled);
		authenticated.setEnabled(enabled);
		blocked.setEnabled(enabled);
		saveButton.setEnabled(enabled);
	}

	@PostConstruct
	public void init() {

		locale = UI.getCurrent().getLocale();

		commandLayout = new HorizontalLayout();
		form = new FormLayout();
		getContent().add(commandLayout, form);
		getContent().setMargin(false);
		commandLayout.add(saveButton);

		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, LabelsPosition.ASIDE),
				new FormLayout.ResponsiveStep("300px", 1, LabelsPosition.ASIDE));

		form.addFormItem(username, "Username");
		form.addFormItem(mobileNumber, "Mobile Nr");
		form.addFormItem(registerDate, "Register date");
		form.addFormItem(authenticated, "Authenticated");
		form.addFormItem(blocked, "Blocked");
		form.addFormItem(authCode, "Authorization code");

		enableFormFields(false);

		saveButton.addClickListener(event -> {
			try {
				binder.writeBean(instance);
				if (instance.getId() == null) {
					playerService.persist(instance);
				} else {
					playerService.update(instance);
				}
				instance = null;
				binder.setBean(instance);
				enableFormFields(false);
				Notifications.showSuccess();
				eventBus.publish(RefreshPlayersGridEvent.REFRESH_GRID_TOPIC, this, new RefreshPlayersGridEvent());
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				// notifyValidationException(e);
			}
		});

		binder.forField(username).bind(e -> e.getUsername(), null);
		binder.forField(mobileNumber).bind(e -> e.getMobileNumber(), User::setMobileNumber);

		binder.forField(registerDate).withConverter(new StringToLocalDateTimeConverter(locale)).bind(
				this::getCreationTime, null);

		binder.forField(authenticated).bind(User::isAuthenticated, User::setAuthenticated);
		binder.forField(blocked).bind(User::isBlocked, User::setBlocked);

		binder.forField(authCode).bind(e -> e.getAuthorizationCode(), null);
	}

	private LocalDateTime getCreationTime(User e) {
		return timeZoneConverter.convertToTarget(e.getRegisterDateTime());
	}

	@EventBusListenerTopic(topic = PlayersGridSelectionEvent.PLAYER_SELECTION_TOPIC)
	@EventBusListenerMethod()
	public void selectionListener(PlayersGridSelectionEvent event) {
		instance = event.getInstance();
		binder.setBean(instance);
		// controls
		if (instance == null) {
			enableFormFields(false);
		} else {
			enableFormFields(true);
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		eventBus.subscribe(this);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		eventBus.unsubscribe(this);
		super.onDetach(detachEvent);
	}
}
