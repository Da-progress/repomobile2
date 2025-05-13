package ch.sofa.lodo.admin.views.gameevents.components;

import ch.sofa.lodo.admin.components.ConfirmationDialog;
import ch.sofa.lodo.admin.views.gameevents.dialogs.SportPickerDialog;
import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.services.EventSportService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class EventSportView extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;
	private final List<EventSport> eventSportsList = new ArrayList<EventSport>();
	private Grid<EventSport> grid;
	private ListDataProvider<EventSport> dataProvider;
	private HorizontalLayout headerlayout = new HorizontalLayout();
	private Button addSportButton = new Button(FontAwesome.Solid.PLUS.create());
	private Button deleteButton = new Button(FontAwesome.Solid.TRASH_ALT.create());

	private Event event;

	@Autowired
	private EventSportService eventSportService;

	@Autowired
	private ConfirmationDialog confirmationDialog;

	@Autowired
	private SportPickerDialog sportPickerDialog;

//	@Autowired
//	private SportService sportService;

	@PostConstruct
	public void init() {
		// eventsList.addAll(eventSportService.findAll());
		getContent().setSizeFull();
		getContent().setMargin(false);

		createComponents();
		buildUI();
	}

	private void createComponents() {

		grid = new Grid<>();
		grid.setHeight("100%"); //$NON-NLS-1$
		grid.setWidth("100%"); //$NON-NLS-1$

		dataProvider = new ListDataProvider<>(eventSportsList);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_WRAP_CELL_CONTENT);

		configureGridColumns();

		grid.setDataProvider(dataProvider);

		grid.addSelectionListener(this::gridSelectionListener);

		deleteButton.setEnabled(false);
		deleteButton.addClickListener(this::deleteSelectedLocation);

		addSportButton.addClickListener(this::addSport);

		sportPickerDialog.addConfirmationListener(e -> addSportListener(e));
	}

	public void configureGridColumns() {

		grid.addColumn(e -> e.getSport()
				.getName())
				.setHeader("Sport")
				.setResizable(true)
				.setSortable(true)
				.setKey("sport-name")
				.setId("sport-name");
	}

	private void buildUI() {
		getContent().setMargin(false);
		getContent().setPadding(false);
		getContent().setHeightFull();
		getContent().add(headerlayout, grid);
		grid.setSizeFull();
		headerlayout.add(addSportButton, deleteButton);
		headerlayout.setWidthFull();
	}

	private void deleteSelectedLocation(ClickEvent<Button> event) {
		confirmationDialog.setQuestion("Remove selected sport from event?");
		confirmationDialog.open();
		confirmationDialog.addConfirmationListener(event2 -> deleteConfirmListener(event2));
	}

	private void deleteConfirmListener(ClickEvent<Button> listener) {
		confirmationDialog.close();
		eventSportService.delete(grid.asSingleSelect()
				.getValue());
		reloadGrid();
	}

	private void gridSelectionListener(SelectionEvent<Grid<EventSport>, EventSport> event) {
		deleteButton.setEnabled(event.getFirstSelectedItem()
				.isPresent());
	}

	private void addSport(ClickEvent<Button> event) {
		sportPickerDialog.setEventSports(eventSportsList);
		sportPickerDialog.open();
	}

	private void addSportListener(ClickEvent<Button> listener) {
		Sport selectedSPort = sportPickerDialog.getSelectedSport();
		sportPickerDialog.close();
		EventSport eventSport = new EventSport();
		eventSport.setEvent(event);
		eventSport.setSport(selectedSPort);
//		event.getEventSports().add(eventSport);
		eventSportService.persist(eventSport);
		reloadGrid();
	}

	public void setEvent(Event event) {
		this.event = event;
		reloadGrid();
	}

	private void reloadGrid() {
		eventSportsList.clear();
		if (event == null || event.getId() == null) {
			addSportButton.setEnabled(false);
		} else {
			addSportButton.setEnabled(true);
			eventSportsList.addAll(eventSportService.findAllByEvent(event));
//			eventSportsList.addAll(event.getEventSports());
		}
		grid.select(null);
		dataProvider.refreshAll();
		getContent().setSizeFull();
	}
}
