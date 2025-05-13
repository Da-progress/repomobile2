package ch.sofa.lodo.admin.views.games.components;

import ch.sofa.lodo.admin.formatters.DateTimeFormatter;
import ch.sofa.lodo.admin.views.games.events.GameGridOnSelectedEvent;
import ch.sofa.lodo.admin.views.games.events.RefreshGamesGridEvent;
import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.services.GameService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.ValueProvider;
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
import java.util.stream.Collectors;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class GamesGrid extends Composite<VerticalLayout> {

	private static final long serialVersionUID = 1L;
	private final List<Game> gameList = new ArrayList<>();
	private Grid<Game> grid;
	private ListDataProvider<Game> dataProvider;
	private HorizontalLayout headerLayout = new HorizontalLayout();
	private HorizontalLayout filtersLayout = new HorizontalLayout();
	private ComboBox<GameStateItem> status = new ComboBox<>();
	@Autowired
	private EventBus.UIEventBus eventBus;
	@Autowired
	private GameService gameService;
	@Autowired
	private DateTimeFormatter dateTimeFormatter;

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

	@PostConstruct
	public void init() {
		gameList.addAll(gameService.findAll());
		getContent().setSizeFull();
		getContent().setMargin(false);

		createComponents();
		buildUI();
	}

	private void buildUI() {
		getContent().add(headerLayout, grid);
		grid.setSizeFull();

		filtersLayout.add(status);
		filtersLayout.setWidthFull();
		filtersLayout.setJustifyContentMode(JustifyContentMode.END);
		headerLayout.add(filtersLayout);
		headerLayout.setWidthFull();
	}

	private void createComponents() {
		grid = new Grid<>();
		grid.setHeight("100%"); //$NON-NLS-1$
		grid.setWidth("100%"); //$NON-NLS-1$

		dataProvider = new ListDataProvider<>(gameList);
		// grid.setItems(projectVersionList);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_WRAP_CELL_CONTENT);

		configureGridColumns();

		grid.setDataProvider(dataProvider);

		grid.addSelectionListener(this::gridSelectionListener);

		List<GameStateItem> statusList = new ArrayList<>();
		GameStateItem nullStatus = new GameStateItem(null, "- All -");
		statusList.add(nullStatus);
		statusList.addAll(GameState.getStates()
				.stream()
				.map(e -> new GameStateItem(e, e.getName()))
				.collect(Collectors.toList()));
		status.setItems(statusList);
		status.setItemLabelGenerator(GameStateItem::getDisplayValue);
		// set default
		status.setValue(nullStatus);

		status.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<?>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChanged(ValueChangeEvent<?> event) {
				reloadGrid((GameStateItem) event.getValue());
			}
		});
	}

	private void gridSelectionListener(SelectionEvent<Grid<Game>, Game> event) {
		publishGridSelection(event.getFirstSelectedItem()
				.orElse(null));
	}

	private void publishGridSelection(Game selectedGame) {
		eventBus.publish("gameSelection", this, new GameGridOnSelectedEvent(selectedGame));
	}

	private void configureGridColumns() {
		grid.addColumn(e -> dateTimeFormatter.formatDateTimeNoSec(e.getExecutionDateTime()))
				.setHeader("Execution")
				.setResizable(true)
				.setSortable(true)
				.setComparator(Comparator.comparing(Game::getExecutionDateTime,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("execution")
				.setId("execution");

		grid.addColumn(this::hostVsGuestLabel)
				.setHeader("Host vs Guest")
				.setResizable(true)
				.setSortable(true)
				.setKey("host-vs-guest")
				.setId("host-vs-guest");

		grid.addColumn(this::hostVsGuestScoreLabel)
				.setHeader("Score")
				.setResizable(true)
				.setSortable(true)
				.setComparator(Comparator.comparing(Game::getHostScore,
						Comparator.nullsFirst(Comparator.naturalOrder()))
						.thenComparing(Game::getGuestScore, Comparator.nullsFirst(Comparator.naturalOrder())))
				.setKey("host-vs-guest-score")
				.setId("host-vs-guest-score");

		grid.addColumn(GetGameLocationOrEventName())
				.setHeader("Location")
				.setSortable(true)
				.setResizable(true)
				.setKey("address")
				.setId("address");

		grid.addColumn(e -> e.getGameState()
				.getName())
				.setHeader("Status")
				.setResizable(true)
				.setSortable(true)
				.setKey("status")
				.setId("status");
	}

	private ValueProvider<Game, ?> GetGameLocationOrEventName() {
		return e -> {
			if (e.getEventSport() == null && e.getLocation() != null) {

				return e.getLocation()
						.getName() + " - "
						+ e.getLocation()
						.getSport()
						.getName();
			} else if (e.getEventSport() != null && e.getLocation() == null) {
				return e.getEventSport()
						.getEvent()
						.getName() + " - "
						+ e.getEventSport()
						.getSport()
						.getName();
			}
			return null;
		};
	}

	private String hostVsGuestLabel(Game game) {
		if (game.getHostPlayer() == null && game.getGuestPlayer() == null) {
			return "???"
					+ " vs ???";
		} else if (game.getGuestPlayer() == null) {
			return game.getHostPlayer()
					.getUsername()
					+ " vs ???";
		} else if (game.getHostPlayer() == null) {
			return "???"
					+ " vs " + game.getGuestPlayer().getUsername();
		}
		return game.getHostPlayer()
				.getUsername()
				+ " vs " + game.getGuestPlayer()
				.getUsername();
	}

	private String hostVsGuestScoreLabel(Game game) {
		if (game.getHostScore() == null || game.getGuestScore() == null) {
			return null;
		}

		return game.getHostScore() + ":" + game.getGuestScore();
	}

	@EventBusListenerTopic(topic = "refreshGameGrid")
	@EventBusListenerMethod()
	public void selectionListener(RefreshGamesGridEvent event) {
		reloadGridItems();
	}

	private void reloadGridItems() {
		reloadGrid(status.getValue());
	}

	private void reloadGrid(GameStateItem status) {
		List<GameState> statuses = new ArrayList<>();
		if (status != null && status.getState() != null) {
			statuses.add(status.getState());
		}
		gameList.clear();
		gameList.addAll(gameService.filterBy(statuses));
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
			grid.select(gameList.stream()
					.filter(e -> e.getId()
							.equals(selectedId))
					.findFirst()
					.orElse(null));
		}
	}

	public static class GameStateItem {

		private GameState state;
		private String displayValue;

		public GameStateItem(GameState state, String displayValue) {
			super();
			this.state = state;
			this.displayValue = displayValue;
		}

		public GameState getState() {
			return state;
		}

		String getDisplayValue() {
			return displayValue;
		}
	}
}
