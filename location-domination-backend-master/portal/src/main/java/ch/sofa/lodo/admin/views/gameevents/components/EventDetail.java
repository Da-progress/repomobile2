package ch.sofa.lodo.admin.views.gameevents.components;

import ch.sofa.lodo.admin.converters.TimeZoneConverter;
import ch.sofa.lodo.admin.notifications.Notifications;
import ch.sofa.lodo.admin.views.gameevents.events.CreteNewEventEvent;
import ch.sofa.lodo.admin.views.gameevents.events.EventGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.gameevents.events.RefreshEventGridEvent;
import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.EventService;
import ch.sofa.lodo.data.services.EventSportService;
import ch.sofa.lodo.data.services.authentications.PortalUserService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class EventDetail extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;

	private String browserTimeZoneId;

	private Tab attributesTab;
	private Tab sportsTab;
	private Map<Tab, Component> tabsToPages;
	private Set<Component> pagesShown;

	private FormLayout form;
	private HorizontalLayout commandLayout;

	private Event instance = new Event();
	private Binder<Event> binder = new Binder<>();

	private TextField name = new TextField();
	private TextField sponsorText = new TextField();
	private DatePicker startDate = new DatePicker();
	private DatePicker endDate = new DatePicker();
	private TimePicker startTime = new TimePicker();
	private TimePicker endTime = new TimePicker();
	private TextField street = new TextField();
	private TextField streetNumber = new TextField();
	private TextField postcode = new TextField();
	private TextField city = new TextField();
	private TextField country = new TextField();
	private NumberField latitude = new NumberField();
	private NumberField longitude = new NumberField();
	private ComboBox<User> creatorName = new ComboBox<>();
	private ComboBox<User> responsibleUser = new ComboBox<>();
	private TextField registrationCode = new TextField();

	private User currentUser;

	private Button saveButton = new Button(FontAwesome.Solid.SAVE.create());

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private EventService eventService;

	@Autowired
	private EventSportService eventSportService;

	@Autowired
	private PortalUserService portalUserService;

	@Autowired
	private TimeZoneConverter timeZoneConverter;

	@Autowired
	private EventSportView eventSportView;

	private Tabs tabs;

	@PostConstruct
	public void init() {

		UI.getCurrent()
				.getPage()
				.retrieveExtendedClientDetails(e -> browserTimeZoneId = e.getTimeZoneId());

		currentUser = portalUserService.getCurrentUser();

		commandLayout = new HorizontalLayout();
		form = new FormLayout();
		getContent().add(commandLayout, form);
		getContent().setMargin(false);
		commandLayout.add(saveButton);

		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, LabelsPosition.ASIDE), new FormLayout.ResponsiveStep("300px", 1, LabelsPosition.ASIDE));

		form.addFormItem(name, "Name");
		form.addFormItem(sponsorText, "Sponsor text");
		form.addFormItem(startDate, "Start date");
		form.addFormItem(startTime, "");
		form.addFormItem(endDate, "End date");
		form.addFormItem(endTime, "");
		form.addFormItem(street, "Street");
		form.addFormItem(streetNumber, "Street number");
		form.addFormItem(postcode, "Postcode");
		form.addFormItem(city, "City");
		form.addFormItem(country, "Country");
		form.addFormItem(latitude, "Latitude");
		form.addFormItem(longitude, "Longitude");
		form.addFormItem(creatorName, "Creator");
		form.addFormItem(responsibleUser, "Responsible");
		form.addFormItem(registrationCode, "Registration code");

		startDate.setLocale(new Locale("de", "CH"));
		endDate.setLocale(new Locale("de", "CH"));

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
				// List<EventSport> updatedSports = instance.getEventSports();
				// Event refreshedInstance = null;
				if (instance.getId() == null) {
					// refreshedInstance = eventService.persist(instance);
					eventService.persist(instance);
				} else {
					// refreshedInstance = eventService.update(instance);
					eventService.update(instance);
				}

				instance = null;
				binder.setBean(instance);
				enableFormFields(false);
				Notifications.showSuccess();
				eventBus.publish("refreshEventGrid", this, new RefreshEventGridEvent());
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				// notifyValidationException(e);
			}
		});

		creatorName.setItems(portalUserService.findAll());
		creatorName.setItemLabelGenerator(e -> e.getUsername());

		responsibleUser.setItems(portalUserService.findAll());
		responsibleUser.setItemLabelGenerator(e -> e.getUsername());

		binder.forField(name)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 50, "Name must less or equal up to 50 characters")
				.bind(Event::getName, Event::setName);
		binder.forField(sponsorText)
				.withValidator(name -> name.length() <= 250, "Text name must less or equal up to 50 characters")
				.bind(Event::getSponsorText, Event::setSponsorText);
		binder.forField(street)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 50, "Street name must less or equal up to 50 characters")
				.bind(e -> e.getAddress()
								.getStreet(),
						(location, value) -> location.getAddress()
								.setStreet(value));
		binder.forField(streetNumber)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 10, "Street number name  must less or equal up to 10 characters")
				.bind(e -> e.getAddress()
								.getStreetNumber(),
						(location, value) -> location.getAddress()
								.setStreetNumber(value));
		binder.forField(city)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 50, "City name  must be less or equal to 50 characters")
				.bind(e -> e.getAddress()
								.getCity(),
						(location, value) -> location.getAddress()
								.setCity(value));
		binder.forField(country)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 4, "Country code must be less or equal to 4 characters")
				.bind(e -> e.getAddress()
								.getCountry(),
						(location, value) -> location.getAddress()
								.setCountry(value));
		binder.forField(postcode)
				.asRequired("Required field")
				.bind(e -> e.getAddress()
								.getPostcode(),
						(location, value) -> location.getAddress()
								.setPostcode(value));

		binder.forField(longitude)
				.asRequired("Required field")
				.bind(Event::getLongitude, Event::setLongitude);
		binder.forField(latitude)
				.asRequired("Required field")
				.bind(Event::getLatitude, Event::setLatitude);

		binder.forField(startDate)
				.asRequired("Required field")
				.bind(this::getStartDate, this::setStartDate);

		binder.forField(startTime)
				.asRequired("Required field")
				.bind(this::getStartTime, this::setStartTime);

		binder.forField(endDate)
				.asRequired("Required field")
				.bind(this::getEndDate, this::setEndDate);

		binder.forField(endTime)
				.asRequired("Required field")
				.bind(this::getEndTime, this::setEndTime);

		binder.forField(creatorName)
				.bind(e -> e.getCreator(), null);

		binder.forField(responsibleUser)
				.asRequired("Required field")
				.bind(Event::getResponsibleUser, Event::setResponsibleUser);

		binder.forField(registrationCode)
				.asRequired("Required field")
				.withValidator(name -> name.length() <= 20, "Registration code must less or equal up to 20 characters")
				.bind(Event::getRegistrationCode, Event::setRegistrationCode);

		configureTabs();
	}

	private LocalDate getStartDate(Event e) {
		LocalDateTime converted = timeZoneConverter.convertToTarget(e.getStartDateTime());
		return converted == null ? null : LocalDate.from(converted);
	}

	private void setStartDate(Event ev, LocalDate date) {
		LocalDateTime target = LocalDateTime.of(date, startTime.getValue() == null ? LocalTime.of(0, 0) : startTime.getValue());
		LocalDateTime source = timeZoneConverter.convertToSource(target);
		ev.setStartDateTime(source);
	}

	private LocalTime getStartTime(Event e) {
		LocalDateTime converted = timeZoneConverter.convertToTarget(e.getStartDateTime());
		return converted == null ? null : LocalTime.from(converted);
	}

	private void setStartTime(Event ev, LocalTime time) {
		LocalDateTime target = LocalDateTime.of(startDate.getValue(), time == null ? LocalTime.of(0, 0) : time);
		LocalDateTime source = timeZoneConverter.convertToSource(target);
		ev.setStartDateTime(source);
	}

	private LocalDate getEndDate(Event e) {
		LocalDateTime converted = timeZoneConverter.convertToTarget(e.getEndDateTime());
		return converted == null ? null : LocalDate.from(converted);
	}

	private void setEndDate(Event ev, LocalDate date) {
		LocalDateTime target = LocalDateTime.of(date, endTime.getValue() == null ? LocalTime.of(0, 0) : endTime.getValue());
		LocalDateTime source = timeZoneConverter.convertToSource(target);
		ev.setEndDateTime(source);
	}

	private LocalTime getEndTime(Event e) {
		LocalDateTime converted = timeZoneConverter.convertToTarget(e.getEndDateTime());
		return converted == null ? null : LocalTime.from(converted);
	}

	private void setEndTime(Event ev, LocalTime time) {
		LocalDateTime target = LocalDateTime.of(endDate.getValue(), time == null ? LocalTime.of(0, 0) : time);
		LocalDateTime source = timeZoneConverter.convertToSource(target);
		ev.setEndDateTime(source);
	}

	private void configureTabs() {
		attributesTab = new Tab("Attributes");
		Component attributesTabContent = form;

		sportsTab = new Tab("Sports");
		Component sportsTabContent = eventSportView;
		sportsTabContent.setVisible(false);

		tabsToPages = new HashMap<>();
		tabsToPages.put(attributesTab, attributesTabContent);
		tabsToPages.put(sportsTab, sportsTabContent);
		tabs = new Tabs(attributesTab, sportsTab);
		VerticalLayout pages = new VerticalLayout(attributesTabContent, sportsTabContent);
		pages.setSizeFull();
		pages.setMargin(false);
		pages.setPadding(false);
		pagesShown = Stream.of(attributesTabContent)
				.collect(Collectors.toSet());

		getContent().add(tabs);
		getContent().add(pages);

		tabs.addSelectedChangeListener(event -> {
			pagesShown.forEach(page -> page.setVisible(false));
			pagesShown.clear();
			Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
			if (selectedPage instanceof EventSportView) {
				((EventSportView) selectedPage).setEvent(instance);
			}
			pagesShown.add(selectedPage);
		});
	}

	private void enableFormFields(boolean enabled) {
		name.setEnabled(enabled);
		sponsorText.setEnabled(enabled);
		startDate.setEnabled(enabled);
		endDate.setEnabled(enabled);
		street.setEnabled(enabled);
		streetNumber.setEnabled(enabled);
		postcode.setEnabled(enabled);
		city.setEnabled(enabled);
		country.setEnabled(enabled);
		latitude.setEnabled(enabled);
		longitude.setEnabled(enabled);
		creatorName.setEnabled(enabled);
		responsibleUser.setEnabled(enabled);
		registrationCode.setEnabled(enabled);
		saveButton.setEnabled(enabled);
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

	@EventBusListenerTopic(topic = "newEventEvent")
	@EventBusListenerMethod()
	public void newEventListener(CreteNewEventEvent event) {
		currentUser = portalUserService.getCurrentUser();
		instance = new Event();
		// default values
		instance.setCreationDateTime(LocalDateTime.now());
		instance.setCreator(currentUser);
		// controls
		enableFormFields(true);

		eventSportView.setEvent(instance);
		binder.setBean(instance);
		name.focus();
	}

	@EventBusListenerTopic(topic = "eventSelection")
	@EventBusListenerMethod()
	public void selectionListener(EventGridOnSelectedEvent event) {
		currentUser = portalUserService.getCurrentUser();
		instance = event.getInstance();
		binder.setBean(instance);
		eventSportView.setEvent(instance);
		// controls
		if (instance == null) {
			enableFormFields(false);
		} else {
			enableFormFields(true);
		}
	}
}
