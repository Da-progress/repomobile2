package ch.sofa.lodo.admin.views.players.components;

import ch.sofa.lodo.admin.components.ConfirmationDialog;
import ch.sofa.lodo.admin.formatters.DateTimeFormatter;
import ch.sofa.lodo.admin.views.players.events.PlayersGridSelectionEvent;
import ch.sofa.lodo.admin.views.players.events.RefreshPlayersGridEvent;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.PlayerService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
public class PlayersGrid extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;
	private final List<User> locationsList = new ArrayList<User>();
	private Grid<User> grid;
	private ListDataProvider<User> dataProvider;
	private HorizontalLayout headerlayout = new HorizontalLayout();
	// private Button newButton = new Button(FontAwesome.Solid.PLUS.create());
	// private Button deleteButton = new Button(FontAwesome.Solid.TRASH_ALT.create());

	private HorizontalLayout filtersLayout = new HorizontalLayout();
	private TextField searchBox = new TextField();
	private Button searchButton = new Button(FontAwesome.Solid.SEARCH.create());

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Autowired
	private PlayerService locationService;

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

		// deleteButton.setEnabled(false);
		// deleteButton.addClickListener(this::deleteSelectedLocation);
		//
		// newButton.addClickListener(this::newButtonClickListener);

	}

	private void newButtonClickListener(ClickEvent<Button> event) {
		grid.select(null);
		// eventBus.publish("newLocationEvent", this, new CreateNewLocationEvent());
	}

	private void reloadGrid() {

		locationsList.clear();
		locationsList.addAll(locationService.findAll());
		Long selectedId = grid.asSingleSelect().getValue() == null ? null : grid.asSingleSelect().getValue().getId();
		grid.select(null);
		dataProvider.refreshAll();
		if (selectedId == null) {
			grid.select(null);
		} else {
			grid.select(locationsList.stream().filter(e -> e.getId().equals(selectedId)).findFirst().orElse(null));
		}
	}

	private void deleteSelectedLocation(ClickEvent<Button> event) {
		confirmationDialog.setQuestion("Delete selected location?");
		confirmationDialog.open();
		confirmationDialog.addConfirmationListener(event2 -> deleteConfirmListener(event2));
	}

	private void deleteConfirmListener(ClickEvent<Button> listener) {
		confirmationDialog.close();
		locationService.delete(grid.asSingleSelect().getValue());
		reloadGridItems();
		grid.select(null);
	}

	private void gridSelectionListener(SelectionEvent<Grid<User>, User> event) {
		// deleteButton.setEnabled(event.getFirstSelectedItem()
		// .isPresent());
		publishGridSelection(event.getFirstSelectedItem().orElse(null));
	}

	private void publishGridSelection(User selectedLocation) {
		eventBus.publish(PlayersGridSelectionEvent.PLAYER_SELECTION_TOPIC, this,
				new PlayersGridSelectionEvent(selectedLocation));
	}

	public void configureGridColumns() {

		grid.addColumn(User::getUsername)
				.setHeader("Username")
				.setResizable(true)
				.setSortable(true)
				.setKey("name")
				.setId("name");

		grid.addColumn(User::getMobileNumber)
				.setHeader("Mobile Nr")
				.setResizable(true)
				.setSortable(true)
				.setKey("mobile-nr")
				.setId("mobile-nr");

		grid.addColumn(e -> dateTimeFormatter.formatDateTimeNoSec(e.getRegisterDateTime()))
				.setHeader("Registered")
				.setSortable(true)
				.setResizable(true)
				.setComparator(Comparator.comparing(User::getRegisterDateTime,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("register-date")
				.setId("register-date");


		grid.addComponentColumn((item) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setEnabled(false);
			checkBox.setValue(item.isAuthenticated());
			return checkBox;
		}).setHeader("Authenticated")
				.setResizable(true)
				.setSortable(true)
				.setKey("authenticated")
				.setId("authenticated");


		grid.addComponentColumn((item) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setEnabled(false);
			checkBox.setValue(item.isBlocked());
			return checkBox;
		}).setHeader("Blocked")
				.setResizable(true)
				.setSortable(true)
				.setKey("blocked")
				.setId("blocked");
		;
	}

	private void buildUI() {
		getContent().add(headerlayout, grid);
		grid.setSizeFull();

		// filtersLayout.add(status, searchBox, searchButton);
		filtersLayout.setWidthFull();
		filtersLayout.setJustifyContentMode(JustifyContentMode.END);
		// headerlayout.add(filtersLayout);
		headerlayout.setWidthFull();
		;
	}

	@EventBusListenerTopic(topic = RefreshPlayersGridEvent.REFRESH_GRID_TOPIC)
	@EventBusListenerMethod()
	public void selectionListener(RefreshPlayersGridEvent event) {
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
