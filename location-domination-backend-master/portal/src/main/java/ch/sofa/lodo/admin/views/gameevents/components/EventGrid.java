package ch.sofa.lodo.admin.views.gameevents.components;

import ch.sofa.lodo.admin.components.ConfirmationDialog;
import ch.sofa.lodo.admin.formatters.DateTimeFormatter;
import ch.sofa.lodo.admin.views.gameevents.dialogs.NewEventDialog;
import ch.sofa.lodo.admin.views.gameevents.events.CreteNewEventEvent;
import ch.sofa.lodo.admin.views.gameevents.events.EventGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.gameevents.events.RefreshEventGridEvent;
import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.services.EventService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class EventGrid extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;
	private final List<Event> eventsList = new ArrayList<Event>();
	private Grid<Event> grid;
	private ListDataProvider<Event> dataProvider;
	private HorizontalLayout headerlayout = new HorizontalLayout();
	private Button newButton = new Button(FontAwesome.Solid.PLUS.create());
	private Button deleteButton = new Button(FontAwesome.Solid.TRASH_ALT.create());

	private HorizontalLayout filtersLayout = new HorizontalLayout();
	private TextField searchBox = new TextField();
	private Button searchButton = new Button(FontAwesome.Solid.SEARCH.create());

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private EventService eventService;

	@Autowired
	private ConfirmationDialog confirmationDialog;

	@Autowired
	private NewEventDialog newEventDialog;

	@Autowired
	private DateTimeFormatter dateTimeFormatter;

	@PostConstruct
	public void init() {
		eventsList.addAll(eventService.findAll());
		getContent().setSizeFull();
		getContent().setMargin(false);

		createComponents();
		buildUI();
	}

	private void createComponents() {

		grid = new Grid<>();
		grid.setHeight("100%"); //$NON-NLS-1$
		grid.setWidth("100%"); //$NON-NLS-1$

		dataProvider = new ListDataProvider<>(eventsList);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_WRAP_CELL_CONTENT);

		configureGridColumns();

		grid.setDataProvider(dataProvider);

		grid.addSelectionListener(this::gridSelectionListener);

		deleteButton.setEnabled(false);
		deleteButton.addClickListener(this::deleteSelectedLocation);

		newButton.addClickListener(this::newButtonClickListener);

		searchButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				reloadGrid();
			}
		});

		confirmationDialog.addConfirmationListener(event2 -> deleteConfirmListener(event2));
		newEventDialog.addConfirmationListener(event3 -> createNewEventClickListener(event3));
		newEventDialog.addCancelClickListener(event3 -> cancelNewEventClickListener(event3));
	}

	private void newButtonClickListener(ClickEvent<Button> event) {
		newEventDialog.open();
	}

	private void createNewEventClickListener(ClickEvent<Button> event) {
		grid.select(null);
		eventBus.publish("newEventEvent", this, new CreteNewEventEvent());
	}

	private void cancelNewEventClickListener(ClickEvent<Button> event) {
		grid.select(null);
		publishGridSelection(null);
	}

	private void reloadGrid() {
		eventsList.clear();
		eventsList.addAll(eventService.filterBy(searchBox.getValue()));
		Long selectedId = grid.asSingleSelect()
				.getValue() == null ? null
				: grid.asSingleSelect()
				.getValue()
				.getId();
		grid.select(null);
		dataProvider.refreshAll();
		if (selectedId == null) {
			grid.select(null);
		} else {
			grid.select(eventsList.stream()
					.filter(e -> e.getId()
							.equals(selectedId))
					.findFirst()
					.orElse(null));
		}
	}

	private void deleteSelectedLocation(ClickEvent<Button> event) {
		confirmationDialog.setQuestion("Delete selected Event?");
		confirmationDialog.open();
	}

	private void deleteConfirmListener(ClickEvent<Button> listener) {
		confirmationDialog.close();
		eventService.delete(grid.asSingleSelect()
				.getValue());
		reloadGridItems();
	}

	private void gridSelectionListener(SelectionEvent<Grid<Event>, Event> event) {
		deleteButton.setEnabled(event.getFirstSelectedItem()
				.isPresent());
		publishGridSelection(event.getFirstSelectedItem()
				.orElse(null));
	}

	private void publishGridSelection(Event selectedLocation) {
		eventBus.publish("eventSelection", this, new EventGridOnSelectedEvent(selectedLocation));
	}

	public void configureGridColumns() {

		grid.addColumn(Event::getName)
				.setHeader("Name")
				.setResizable(true)
				.setSortable(true)
				.setKey("name")
				.setId("name");

		grid.addColumn(e -> dateTimeFormatter.formatDateTimeNoSec(e.getStartDateTime()))
				.setHeader("Start date")
				.setResizable(true)
				.setSortable(true)
				.setComparator(Comparator.comparing(Event::getStartDateTime,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("start-date")
				.setId("start-date");

		grid.addColumn(e -> dateTimeFormatter.formatDateTimeNoSec(e.getEndDateTime()))
				.setHeader("End date")
				.setResizable(true)
				.setSortable(true)
				.setComparator(Comparator.comparing(Event::getEndDateTime,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("end-date")
				.setId("end-date");

		grid.addColumn(e -> e.getAddress()
				.getStreet() + " "
				+ e.getAddress()
				.getStreetNumber()
				+ ", " + e.getAddress()
				.getPostcode()
				+ " " + e.getAddress()
				.getCity())
				.setHeader("Address")
				.setSortable(true)
				.setResizable(true)
				.setKey("address")
				.setId("address");

		grid.addColumn(e -> e.getCreator()
				.getFirstName())
				.setHeader("Responsible")
				.setResizable(true)
				.setSortable(true)
				.setKey("responsible-name")
				.setId("responsible-name");
	}

	private void buildUI() {
		getContent().add(headerlayout, grid);
		grid.setSizeFull();

		filtersLayout.add(searchBox, searchButton);
		filtersLayout.setWidthFull();
		filtersLayout.setJustifyContentMode(JustifyContentMode.END);
		headerlayout.add(newButton, deleteButton, filtersLayout);
		headerlayout.setWidthFull();
	}

	@EventBusListenerTopic(topic = "refreshEventGrid")
	@EventBusListenerMethod()
	public void selectionListener(RefreshEventGridEvent event) {
		reloadGridItems();
	}

	private void reloadGridItems() {
		reloadGrid();
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
