package ch.sofa.lodo.admin.views.login;

import ch.sofa.lodo.admin.configurations.SecurityUtils;
import ch.sofa.lodo.admin.i18n.I18N;
import ch.sofa.lodo.admin.session.ClientBrowserData;
import ch.sofa.lodo.admin.utils.GetBrowserLocale;
import ch.sofa.lodo.admin.utils.Message;
import ch.sofa.lodo.admin.views.gameevents.EventsView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.ExtendedClientDetails;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.util.Locale;

@SuppressWarnings("serial")
@CssImport(value = "./styles/login-theme.css")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
@Tag("vaadin-login-view")
public class LoginView extends Composite<Div> implements PageConfigurator, AfterNavigationObserver {

	public static final String ROUTE = "login";

	private VerticalLayout loginFormLayout;
	private Label loginLabel;
	private TextField userName;
	private PasswordField password;
	private Button loginButton;

	@Autowired
	private I18N i18N;

	@Autowired
	private ClientBrowserData clientBrowserData;

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;

	private Locale locale;

	public LoginView() {

	}

	@PostConstruct
	public void init() {

		this.locale = GetBrowserLocale.getBrowserLocale(UI.getCurrent()
				.getSession()
				.getBrowser());

		Div loginInformation = buildLoginInformation();
		loginInformation.getClassNames()
				.add("login-animation");

		loginFormLayout = buildLoginFormLayout();

		HorizontalLayout root = new HorizontalLayout();
		root.add(loginInformation, loginFormLayout);
		root.setSizeFull();
		root.setFlexGrow(1.0d, loginFormLayout);

		userName.focus();

		getContent().add(root);
		getContent().setSizeFull();
	}

	private Div buildLoginInformation() {

		Div loginInformation = new Div();
		loginInformation.getClassNames()
				.add("login-information");

		Label brandingLabel = new Label(i18N.getTranslation("LoginUI.application.title", locale));
		brandingLabel.getClassNames()
				.add("branding-label");

		Label companyNameLabel = new Label(i18N.getTranslation("LoginUI.company.name", locale));
		companyNameLabel.getClassNames()
				.add("login-info-label");

		Label companyStreetLabel = new Label(i18N.getTranslation("LoginUI.company.street", locale));
		companyStreetLabel.getClassNames()
				.add("login-info-label");

		Label companyCityLabel = new Label(i18N.getTranslation("LoginUI.company.city", locale));
		companyCityLabel.getClassNames()
				.add("login-info-label");

		Label companyContactLabel = new Label(i18N.getTranslation("LoginUI.company.contact", locale));
		companyContactLabel.getClassNames()
				.add("login-info-label");

		Label companyPhoneLabel = new Label(i18N.getTranslation("LoginUI.company.phone", locale));
		companyPhoneLabel.getClassNames()
				.add("login-info-label");

		VerticalLayout labelLayout = new VerticalLayout(companyNameLabel, companyStreetLabel, companyCityLabel, companyContactLabel, companyPhoneLabel);
		labelLayout.addComponentAsFirst(brandingLabel);
		labelLayout.setSpacing(false);

		loginInformation.add(labelLayout);
		loginInformation.setSizeUndefined();

		return loginInformation;
	}

	private VerticalLayout buildLoginFormLayout() {

		VerticalLayout rootFormLayout = new VerticalLayout();
		rootFormLayout.setMargin(false);
		rootFormLayout.setPadding(false);
		rootFormLayout.setSizeFull();
		rootFormLayout.getStyle()
				.set("background-image", "url(frontend/img/login-bg.jpg)");

		FormLayout formLayout = new FormLayout();
		formLayout.setMaxWidth("35%");
		formLayout.addClassName("login-form");

		createLoginFormComponents();

		formLayout.add(loginLabel, userName, password, loginButton);

		HorizontalLayout centeringLayout = new HorizontalLayout(formLayout);
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		rootFormLayout.add(centeringLayout);
		rootFormLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		rootFormLayout.setHorizontalComponentAlignment(Alignment.CENTER, centeringLayout);

		return rootFormLayout;
	}

	private void createLoginFormComponents() {

		loginLabel = new Label(i18N.getTranslation("LoginFormFactory.login", locale));
		loginLabel.getClassNames()
				.add("login-label");

		loginButton = new Button(i18N.getTranslation("LoginFormFactory.login", locale));
		loginButton.addClickShortcut(Key.ENTER);
		loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
		loginButton.setWidth("100%");
		loginButton.setId("btnLogin");
		loginButton.addClickListener(event -> handleLogin());

		userName = new TextField(i18N.getTranslation("LoginFormFactory.username", locale));
		userName.getClassNames()
				.add("login-textfields");
		userName.addThemeVariants(TextFieldVariant.LUMO_SMALL);

		password = new PasswordField(i18N.getTranslation("LoginFormFactory.password", locale));
		password.getClassNames()
				.add("login-textfields");
		password.addThemeVariants(TextFieldVariant.LUMO_SMALL);

		userName.setId("tfUsername"); //$NON-NLS-1$
		password.setId("tfPassword"); //$NON-NLS-1$
	}

	private void handleLogin() {

		// async call
		UI.getCurrent()
				.getPage()
				.retrieveExtendedClientDetails(this::handleLoggin2);

		//handleLoggin2();

	}

	private void handleLoggin2(ExtendedClientDetails details) {
		clientBrowserData.setDetails(details);

		try {

			Authentication auth = new UsernamePasswordAuthenticationToken(userName.getValue(), password.getValue());
			Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
			SecurityContextHolder.getContext()
					.setAuthentication(authenticated);

			UI.getCurrent()
					.navigate(EventsView.ROUTE);
		} catch (AuthenticationException e) {
			Message.showError(i18N.getTranslationWithAppLocale("LoginFormFactory.loginerror"));
		}

		userName.clear();
		password.clear();
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		LoadingIndicatorConfiguration conf = settings.getLoadingIndicatorConfiguration();
		conf.setFirstDelay(300);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			getUI().ifPresent(ui -> ui.getPage()
					.executeJs("location.assign('logout')"));
		}
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}
}
