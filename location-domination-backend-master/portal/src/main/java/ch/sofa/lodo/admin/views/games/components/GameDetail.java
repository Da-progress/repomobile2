package ch.sofa.lodo.admin.views.games.components;

import ch.sofa.lodo.admin.converters.StringToIntegerWithNullConverter;
import ch.sofa.lodo.admin.converters.StringToLocalDateTimeConverter;
import ch.sofa.lodo.admin.converters.TimeZoneConverter;
import ch.sofa.lodo.admin.notifications.Notifications;
import ch.sofa.lodo.admin.session.ClientBrowserData;
import ch.sofa.lodo.admin.validators.StringAsOnlyInetegerValidator;
import ch.sofa.lodo.admin.views.games.events.GameGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.games.events.RefreshGamesGridEvent;
import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.GameService;
import ch.sofa.lodo.data.services.authentications.PortalUserService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
public class GameDetail extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;

	private FormLayout form;
	private HorizontalLayout commandLayout;

	private Game instance = new Game();
	private Binder<Game> binder = new Binder<>();

	private TextField executionDate = new TextField();
	private TextField hostPlayer = new TextField();
	private TextField guestPlayer = new TextField();
	private TextField location = new TextField();
	// private TextField event = new TextField();
	private TextField hostScore = new TextField();
	private TextField guestScore = new TextField();
	// private TextField season = new TextField();
	private ComboBox<GameState> status = new ComboBox<>();
	private TextField creationDateTime = new TextField();
	private TextField acceptDateTime = new TextField();
	private TextField resultDateTime = new TextField();
	private TextField verifiedDateTime = new TextField();
	private TextField suggestionFirstDateTime = new TextField();
	private TextField suggestionSecondDateTime = new TextField();
	private TextField suggestionThirdDateTime = new TextField();

	private Button saveButton = new Button(FontAwesome.Solid.SAVE.create());

	private User currentUser;

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private GameService gameService;

	@Autowired
	private PortalUserService portalUserService;

	@Autowired
	private TimeZoneConverter timeZoneConverter;

	@Autowired
	private ClientBrowserData clientBrowserData;

	private Locale locale;

	private void enableFormFields(boolean enabled) {
		hostScore.setEnabled(enabled);
		guestScore.setEnabled(enabled);
		status.setEnabled(enabled && currentUser.getIsAdmin());
	}

	@PostConstruct
	public void init() {

		locale = UI.getCurrent().getLocale();

		currentUser = portalUserService.getCurrentUser();

		commandLayout = new HorizontalLayout();
		form = new FormLayout();
		getContent().add(commandLayout, form);
		getContent().setMargin(false);
		commandLayout.add(saveButton);

		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, LabelsPosition.ASIDE),
				new FormLayout.ResponsiveStep("300px", 1, LabelsPosition.ASIDE));

		form.addFormItem(executionDate, "Execution time");
		form.addFormItem(hostPlayer, "Host player");
		form.addFormItem(guestPlayer, "Guest player");
		form.addFormItem(location, "Location");
		// form.addFormItem(event, "Event");
		form.addFormItem(hostScore, "Host score");
		form.addFormItem(guestScore, "Guest score");
		// form.addFormItem(season, "Season");
		form.addFormItem(status, "Status");
		form.addFormItem(creationDateTime, "Created");
		form.addFormItem(acceptDateTime, "Accepted");
		form.addFormItem(resultDateTime, "Result entered");
		form.addFormItem(verifiedDateTime, "Verified");
		form.addFormItem(suggestionFirstDateTime, "Suggestion 1");
		form.addFormItem(suggestionSecondDateTime, "Suggestion 2");
		form.addFormItem(suggestionThirdDateTime, "Suggestion 3");

		enableFormFields(false);

		saveButton.addClickListener(event -> {
			try {
				binder.writeBean(instance);
				if (instance.getId() == null) {
					gameService.persist(instance, false);
				} else {
					gameService.update(instance, false);
				}
				instance = null;
				binder.setBean(null);
				enableFormFields(false);
				Notifications.showSuccess();
				eventBus.publish("refreshGameGrid", this, new RefreshGamesGridEvent());
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				// notifyValidationException(e);
			}
		});

		status.setItems(GameState.getStates());
		status.setItemLabelGenerator(GameState::getName);

		binder.forField(executionDate)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getExecutionDateTime, null);

		binder.forField(creationDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getCreationDateTime, null);

		binder.forField(acceptDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getAcceptDateTime, null);

		binder.forField(resultDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getResultRecordDateTime, null);

		binder.forField(verifiedDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getVerifiedDateTime, null);

		binder.forField(hostScore)
				.withValidator(new StringAsOnlyInetegerValidator("Enter positive number"))
				.withConverter(new StringToIntegerWithNullConverter("Enter positive number"))
				.bind(Game::getHostScore, Game::setHostScore);

		binder.forField(guestScore)
				.withValidator(new StringAsOnlyInetegerValidator("Enter positive number"))
				.withConverter(new StringToIntegerWithNullConverter("Enter positive number"))
				.bind(Game::getGuestScore, Game::setGuestScore);

		binder.forField(hostPlayer)
				.bind(e -> e.getHostPlayer()
						.getUsername(), null);

		binder.forField(guestPlayer)
				.bind(e -> e.getGuestPlayer() == null ? null
								: e.getGuestPlayer()
								.getUsername(),
						null);

		binder.forField(status)
				.bind(Game::getGameState, Game::setGameState);

		binder.forField(location)
				.bind(e -> e.getLocation()
								.getName() + " - "
								+ e.getLocation()
								.getSport()
								.getName(),
						null);

		binder.forField(suggestionFirstDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getAppointment1, null);

		binder.forField(suggestionSecondDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getAppointment2, null);

		binder.forField(suggestionThirdDateTime)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getAppointment3, null);
	}

	private LocalDateTime getAppointment1(Game e) {
		return timeZoneConverter.convertToTarget(e.getAppointment1());
	}

	private LocalDateTime getAppointment2(Game e) {
		return timeZoneConverter.convertToTarget(e.getAppointment2());
	}

	private LocalDateTime getAppointment3(Game e) {
		return timeZoneConverter.convertToTarget(e.getAppointment3());
	}

	private LocalDateTime getExecutionDateTime(Game e) {
		return timeZoneConverter.convertToTarget(e.getExecutionDateTime());
	}

	private LocalDateTime getCreationDateTime(Game e) {
		return timeZoneConverter.convertToTarget(e.getCreationDateTime());
	}

	private LocalDateTime getAcceptDateTime(Game e) {
		return timeZoneConverter.convertToTarget(e.getAcceptDateTime());
	}

	private LocalDateTime getResultRecordDateTime(Game e) {
		return timeZoneConverter.convertToTarget(e.getResultRecordDateTime());
	}

	private LocalDateTime getVerifiedDateTime(Game e) {
		return timeZoneConverter.convertToTarget(e.getVerifiedDateTime());
	}

	@EventBusListenerTopic(topic = "gameSelection")
	@EventBusListenerMethod()
	public void selectionListener(GameGridOnSelectedEvent event) {
		instance = event.getGame();
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
