package ch.sofa.lodo.admin.views.locations.components;

import ch.sofa.lodo.admin.components.ConfirmationDialog;
import ch.sofa.lodo.admin.formatters.DateTimeFormatter;
import ch.sofa.lodo.admin.views.locations.events.CreateNewLocationEvent;
import ch.sofa.lodo.admin.views.locations.events.LocationGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.locations.events.RefreshLocationGridEvent;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationState;
import ch.sofa.lodo.data.services.LocationService;
import ch.sofa.lodo.data.services.LocationStateService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
public class LocationsGrid extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;
	private final List<Location> locationsList = new ArrayList<>();
	private final HorizontalLayout headerLayout = new HorizontalLayout();
	private final Button newButton = new Button(FontAwesome.Solid.PLUS.create());
	private final Button deleteButton = new Button(FontAwesome.Solid.TRASH_ALT.create());
	private final HorizontalLayout filtersLayout = new HorizontalLayout();
	private final ComboBox<LocationState> status = new ComboBox<>();
	private final TextField searchBox = new TextField();
	private final Button searchButton = new Button(FontAwesome.Solid.SEARCH.create());
	private Grid<Location> grid;
	private ListDataProvider<Location> dataProvider;
	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationStateService locationStateService;

	@Autowired
	private ConfirmationDialog confirmationDialog;

	@Autowired
	private DateTimeFormatter dateTimeFormatter;

	@PostConstruct
	public void init() {
		locationsList.addAll(locationService.findAll());
		getContent().setSizeFull();
		getContent().setMargin(false);

		createComponents();
		buildUI();
	}

	private void createComponents() {

		grid = new Grid<>();
		grid.setHeight("100%"); //$NON-NLS-1$
		grid.setWidth("100%"); //$NON-NLS-1$

		dataProvider = new ListDataProvider<>(locationsList);
		// grid.setItems(projectVersionList);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_WRAP_CELL_CONTENT);

		configureGridColumns();

		grid.setDataProvider(dataProvider);

		grid.addSelectionListener(this::gridSelectionListener);

		deleteButton.setEnabled(false);
		deleteButton.addClickListener(this::deleteSelectedLocation);

		newButton.addClickListener(this::newButtonClickListener);

		List<LocationState> statusList = new ArrayList<>();
		LocationState nullStatus = new LocationState();
		nullStatus.setName("- All -");
		statusList.add(nullStatus);
		statusList.addAll(locationStateService.findAll());
		status.setItems(statusList);
		status.setItemLabelGenerator(LocationState::getName);
		// set default
		status.setValue(nullStatus);

		status.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<?>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChanged(ValueChangeEvent<?> event) {
				reloadGrid((LocationState) event.getValue());
			}
		});

		searchButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				reloadGrid(status.getValue());
			}
		});
	}

	private void newButtonClickListener(ClickEvent<Button> event) {
		grid.select(null);
		eventBus.publish("newLocationEvent", this, new CreateNewLocationEvent());
	}

	private void reloadGrid(LocationState status) {
		List<LocationState> statuses = new ArrayList<>();
		if (status != null && status.getId() != null) {
			statuses.add(status);
		}
		locationsList.clear();
		locationsList.addAll(locationService.filterByLocationState(statuses, searchBox.getValue()));
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
			grid.select(locationsList.stream()
					.filter(e -> e.getId()
							.equals(selectedId))
					.findFirst()
					.orElse(null));
		}
	}

	private void deleteSelectedLocation(ClickEvent<Button> event) {
		confirmationDialog.setQuestion("Delete selected location?");
		confirmationDialog.open();
		confirmationDialog.addConfirmationListener(this::deleteConfirmListener);
	}

	private void deleteConfirmListener(ClickEvent<Button> listener) {
		confirmationDialog.close();
		locationService.delete(grid.asSingleSelect()
				.getValue());
		reloadGridItems();
		grid.select(null);
	}

	private void gridSelectionListener(SelectionEvent<Grid<Location>, Location> event) {
		deleteButton.setEnabled(event.getFirstSelectedItem()
				.isPresent());
		publishGridSelection(event.getFirstSelectedItem()
				.orElse(null));
	}

	private void publishGridSelection(Location selectedLocation) {
		eventBus.publish("locationSelection", this, new LocationGridOnSelectedEvent(selectedLocation));
	}

	public void configureGridColumns() {

		grid.addColumn(Location::getName)
				.setHeader("Name")
				.setResizable(true)
				.setSortable(true)
				.setKey("name")
				.setId("name");

		grid.addColumn(e -> e.getSport()
				.getName())
				.setHeader("Sport")
				.setResizable(true)
				.setSortable(true)
				.setKey("sport-name")
				.setId("sport-name");

		grid.addColumn(e -> e.getAddress()
				.getStreet() + " "
				+ e.getAddress().getStreetNumber()
				+ ", " + e.getAddress().getPostcode()
				+ " " + e.getAddress().getCity())
				.setHeader("Address")
				.setSortable(true)
				.setResizable(true)
				.setKey("address")
				.setId("address");

		grid.addColumn(e -> e.getLocationState().getName())
				.setHeader("Location state")
				.setResizable(true)
				.setSortable(true)
				.setKey("location-state")
				.setId("location-state");

		grid.addColumn(e -> dateTimeFormatter.formatDateTimeNoSec(e.getCreationDateTime()))
				.setHeader("Creation date")
				.setResizable(true)
				.setSortable(true)
				.setComparator(Comparator.comparing(Location::getCreationDateTime,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("creation-date")
				.setId("creation-date");

		grid.addColumn(e -> e.getCreator().getUsername())
				.setHeader("Creator")
				.setResizable(true)
				.setSortable(true)
				.setKey("creator-name")
				.setId("creator-name");
	}

	private void buildUI() {
		getContent().add(headerLayout, grid);
		grid.setSizeFull();

		filtersLayout.add(status, searchBox, searchButton);
		filtersLayout.setWidthFull();
		filtersLayout.setJustifyContentMode(JustifyContentMode.END);
		headerLayout.add(newButton, deleteButton, filtersLayout);
		headerLayout.setWidthFull();
	}

	@EventBusListenerTopic(topic = "refreshLocationGrid")
	@EventBusListenerMethod()
	public void selectionListener(RefreshLocationGridEvent event) {
		reloadGridItems();
	}

	private void reloadGridItems() {
		reloadGrid(status.getValue());
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
