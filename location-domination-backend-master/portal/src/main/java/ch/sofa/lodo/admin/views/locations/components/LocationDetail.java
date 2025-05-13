package ch.sofa.lodo.admin.views.locations.components;

import ch.sofa.lodo.admin.converters.StringToLocalDateTimeConverter;
import ch.sofa.lodo.admin.converters.TimeZoneConverter;
import ch.sofa.lodo.admin.notifications.Notifications;
import ch.sofa.lodo.admin.views.locations.events.CreateNewLocationEvent;
import ch.sofa.lodo.admin.views.locations.events.LocationGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.locations.events.RefreshLocationGridEvent;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationState;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.LocationService;
import ch.sofa.lodo.data.services.LocationStateService;
import ch.sofa.lodo.data.services.PlayerService;
import ch.sofa.lodo.data.services.SportService;
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
import com.vaadin.flow.component.textfield.NumberField;
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
public class LocationDetail extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;

	private FormLayout form;
	private HorizontalLayout commandLayout;

	private Location instance = new Location();
	private Binder<Location> binder = new Binder<>();

	private TextField locationName = new TextField();
	private ComboBox<Sport> sport = new ComboBox<>();
	private TextField street = new TextField();
	private TextField streetNumber = new TextField();
	private TextField postcode = new TextField();
	private TextField city = new TextField();
	private TextField country = new TextField();
	private NumberField latitude = new NumberField();
	private NumberField longitude = new NumberField();
	private ComboBox<LocationState> locationState = new ComboBox<>();
	// read only
	private TextField creationDate = new TextField();
	private ComboBox<User> creatorName = new ComboBox<>();

	private Button saveButton = new Button(FontAwesome.Solid.SAVE.create());

	private User currentUser;

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private SportService sportService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationStateService locationStateService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private TimeZoneConverter timeZoneConverter;

	@Autowired
	private PortalUserService portalUserService;

	private Locale locale;

	private void enableFormFields(boolean enabled) {
		locationName.setEnabled(enabled);
		sport.setEnabled(enabled);
		street.setEnabled(enabled);
		streetNumber.setEnabled(enabled);
		postcode.setEnabled(enabled);
		city.setEnabled(enabled);
		country.setEnabled(enabled);
		latitude.setEnabled(enabled);
		longitude.setEnabled(enabled);
		locationState.setEnabled(enabled && currentUser.getIsAdmin());
		creatorName.setEnabled(enabled);
		saveButton.setEnabled(enabled);
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

		form.addFormItem(locationName, "Name");
		form.addFormItem(sport, "Sport");
		form.addFormItem(street, "Street");
		form.addFormItem(streetNumber, "Streetnumber");
		form.addFormItem(postcode, "Postcode");
		form.addFormItem(city, "City");
		form.addFormItem(country, "Country");
		form.addFormItem(latitude, "Latitude");
		form.addFormItem(longitude, "Longitude");
		form.addFormItem(locationState, "Status");
		form.addFormItem(creationDate, "Creation date");
		form.addFormItem(creatorName, "Creator");

		longitude.setMin(-180.0);
		latitude.setMin(-90.0);

		longitude.setMax(180.0);
		latitude.setMax(90.0);

		longitude.setErrorMessage("Must be between -180 and +180");
		latitude.setErrorMessage("Must be between -90 and +90");

		enableFormFields(false);

		saveButton.addClickListener(event -> {
			try {
				binder.writeBean(instance);
				if (instance.getId() == null) {
					locationService.persist(instance);
				} else {
					locationService.update(instance);
				}
				instance = null;
				binder.setBean(instance);
				enableFormFields(false);
				Notifications.showSuccess();
				eventBus.publish("refreshLocationGrid", this, new RefreshLocationGridEvent());
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				// notifyValidationException(e);
			}
		});

		locationState.setItems(locationStateService.findAll());
		locationState.setItemLabelGenerator(e -> e.getName());

		sport.setItems(sportService.findAll());
		sport.setItemLabelGenerator(e -> e.getName());

		creatorName.setItems(playerService.findAll());
		creatorName.setItemLabelGenerator(e -> e.getUsername());

		binder.forField(locationName)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 50, "Name must less or equal up to 50 characters")
				.bind(Location::getName, Location::setName);
		binder.forField(sport)
				.asRequired("Required field")
				.bind(Location::getSport, Location::setSport);
		binder.forField(street)
				.withValidator(name -> name.length() <= 50, "Street name must less or equal up to 50 characters")
				.bind(e -> e.getAddress()
								.getStreet(),
						(location, value) -> location.getAddress()
								.setStreet(value));
		binder.forField(streetNumber)
				.withValidator(name -> name.length() <= 10,
						"Street number name  must less or equal up to 10 characters")
				.bind(e -> e.getAddress().getStreetNumber(),
						(location, value) -> location.getAddress().setStreetNumber(value));
		binder.forField(city)
				.withValidator(name -> name.length() <= 50, "City name  must be less or equal to 50 characters")
				.bind(e -> e.getAddress().getCity(),
						(location, value) -> location.getAddress().setCity(value));
		binder.forField(country)
				.withValidator(name -> name.length() <= 4, "Country code must be less or equal to 4 characters")
				.bind(e -> e.getAddress().getCountry(),
						(location, value) -> location.getAddress().setCountry(value));
		binder.forField(postcode)
				.bind(e -> e.getAddress().getPostcode(),
						(location, value) -> location.getAddress().setPostcode(value));

		binder.forField(longitude)
				.asRequired("Required field")
				.bind(Location::getLongitude, Location::setLongitude);
		binder.forField(latitude)
				.asRequired("Required field")
				.bind(Location::getLatitude, Location::setLatitude);

		binder.forField(locationState)
				.asRequired("Required field")
				.bind(Location::getLocationState, (location, value) -> location.setLocationState(value));

		binder.forField(creationDate)
				.withConverter(new StringToLocalDateTimeConverter(locale))
				.bind(this::getCreationTime, null);
		binder.forField(creatorName)
				.bind(Location::getCreator, null);
	}

	private LocalDateTime getCreationTime(Location e) {
		return timeZoneConverter.convertToTarget(e.getCreationDateTime());
	}

	@EventBusListenerTopic(topic = "locationSelection")
	@EventBusListenerMethod()
	public void selectionListener(LocationGridOnSelectedEvent event) {
		currentUser = portalUserService.getCurrentUser();
		instance = event.getLocation();
		binder.setBean(instance);
		// controls
		if (instance == null) {
			enableFormFields(false);
		} else {
			enableFormFields(true);
		}
	}

	@EventBusListenerTopic(topic = "newLocationEvent")
	@EventBusListenerMethod()
	public void newLocationListener(CreateNewLocationEvent event) {
		currentUser = portalUserService.getCurrentUser();
		instance = new Location();
		// default values
		instance.setCreationDateTime(LocalDateTime.now());
		instance.setCreator(currentUser);
		instance.setLocationState(locationStateService.findById(3l));
		// controls
		enableFormFields(true);

		binder.setBean(instance);
		locationName.focus();
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
