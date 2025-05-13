package ch.sofa.lodo.admin.views;

import ch.sofa.lodo.admin.i18n.I18N;
import ch.sofa.lodo.admin.utils.Message;
import ch.sofa.lodo.admin.views.gameevents.EventsView;
import ch.sofa.lodo.admin.views.games.GamesView;
import ch.sofa.lodo.admin.views.locations.LocationsView;
import ch.sofa.lodo.admin.views.players.PlayersView;
import ch.sofa.lodo.data.constants.AppLocale;
import ch.sofa.lodo.data.services.authentications.PortalUserService;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayout.Section;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Locale;

@Route("")
public class MainView extends Composite<Div> implements RouterLayout {

	private static final long serialVersionUID = 1L;

	private AppLayout appLayout = new AppLayout();

	private Tabs menu;
	private Tab events;
	private Tab locations;
	private Tab games;
	private Tab sports;
	private Tab players;
	private Tab seasons;
	private Button logout;
	private Button logoBtn;

	@Autowired
	private I18N i18N;

	@Autowired
	private PortalUserService userService;

	public MainView() {

	}

	@PostConstruct
	private void init() {

		UI.getCurrent()
				.getSession()
				.setLocale(new Locale(AppLocale.DE_CH.getLanguage(), AppLocale.DE_CH.getCountry()));

		setSystemMessages();

		menu = new Tabs();
		menu.setWidthFull();

		appLayout.setPrimarySection(Section.NAVBAR);
		appLayout.setDrawerOpened(false);

		events = new Tab();
		Button eventsButton = new Button("Events", FontAwesome.Solid.CALENDAR_ALT.create(), e -> navigationEvent(EventsView.ROUTE));
		eventsButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		events.add(eventsButton);
		menu.add(events);

		locations = new Tab();
		Button locationsButton = new Button("Locations", FontAwesome.Solid.MAP_MARKER_ALT.create(), e -> navigationEvent(LocationsView.ROUTE));
		locationsButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		locations.add(locationsButton);
		menu.add(locations);

		games = new Tab();
		Button gamesButton = new Button("Games", FontAwesome.Solid.GAMEPAD.create(), e -> navigationEvent(GamesView.ROUTE));
		gamesButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		games.add(gamesButton);
		menu.add(games);

		players = new Tab();
		Button playersButton = new Button("Players", FontAwesome.Solid.USERS.create(), e -> navigationEvent(PlayersView.ROUTE));
		playersButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		players.add(playersButton);
		menu.add(players);

		logout = new Button("Logout");
		logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		logout.addClickListener(e -> logout());
		logout.setWidth("6rem");

		logoBtn = new Button(FontAwesome.Solid.QUESTION_CIRCLE.create());
		logoBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		logoBtn.setWidth("300px");
		logoBtn.setHeight("50px");
		logoBtn.addClickListener(e -> {
			Message.showLongSuccess("Logo Button Clicked!");
		});

		appLayout.addToNavbar(menu, logout, logoBtn);

		getContent().add(appLayout);
		getContent().setSizeFull();
	}

	private void setSystemMessages() {

		// UI.getCurrent().getSession().setErrorHandler((ErrorHandler) event -> {
		//
		// event.getThrowable().printStackTrace();
		// Message.showException(i18N.getTranslationWithAppLocale("global.error.message"),
		// getRootExceptionMessage(event.getThrowable()));
		//
		// });

	}

	private Throwable getRootExceptionMessage(Throwable throwable) {

		if (throwable.getCause() != null) {
			return getRootExceptionMessage(throwable.getCause());
		}

		if (throwable.getClass()
				.equals(IllegalStateException.class)) {
			return new Exception(i18N.getTranslationWithAppLocale("global.error.message.sql.chenged"));
		}

		if (throwable.getClass()
				.equals(ValidationException.class)) {
			return new Exception(throwable.getMessage());
		}

		if (throwable.getClass()
				.equals(SQLIntegrityConstraintViolationException.class)) {
			return new Exception(i18N.getTranslationWithAppLocale("global.error.message.sql.chenged"));
		}

		return throwable;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
	}

	@Override
	public void showRouterLayoutContent(HasElement hasElement) {
		if (hasElement != null && hasElement.getElement()
				.getComponent()
				.isPresent()) {
			appLayout.setContent(hasElement.getElement()
					.getComponent()
					.get());
		}
	}

	private void logout() {
		getUI().ifPresent(ui -> ui.getPage()
				.executeJs("location.assign('logout')"));
	}

	private void navigationEvent(String route) {
		getUI().ifPresent(ui -> ui.navigate(route));
	}
}
