package ch.sofa.lodo.admin.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final List<String> PERMIT_ALL_ENDPOINT_LIST = Collections.singletonList(
			"/actuator/**"
	);

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/login";
	private static final String SUCCESS_URL = "/";

	@Qualifier("userDetailsServiceImpl")
	@Autowired
	private UserDetailsService userDetailService;

	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.inMemoryAuthentication()
	// .withUser("testuser")
	// .password("{noop}testpass")
	// .roles("USER");
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf()
				.disable()
				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache()
				.requestCache(new CustomRequestCache())

				//Health check
				.and()
				.authorizeRequests()
				.antMatchers(PERMIT_ALL_ENDPOINT_LIST.toArray(new String[PERMIT_ALL_ENDPOINT_LIST.size()]))
				.permitAll()

				.and()
				.authorizeRequests()
				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest)
				.permitAll()
				.anyRequest()
				.authenticated()
				// Configure the login page.
				.and()
				.formLogin()
				.loginPage(LOGIN_URL)
				.permitAll()
				.defaultSuccessUrl(SUCCESS_URL, true)
				.loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)

				// Configure logout
				.and()
				.logout()
				.logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
		return new CustomBasicAuthenticationEntryPoint();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		auth.userDetailsService(userDetailService)
				.and()
				.authenticationProvider(authenticationProvider);
	}

	@Bean
	public DaoAuthenticationProvider createDaoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
				.antMatchers(
						// Vaadin Flow static resources
						"/VAADIN/**",

						// the standard favicon URI
						"/favicon.ico",

						// the robots exclusion standard
						"/robots.txt",

						// web application manifest
						"/manifest.webmanifest", "/sw.js", "/offline-page.html",

						// icons and images
						"/icons/**", "/images/**",

						// (development mode) static resources
						"/frontend/**",

						// (development mode) webjars
						"/webjars/**",

						// (development mode) H2 debugging console
						"/h2-console/**",

						// (production mode) static resources
						"/frontend-es5/**", "/frontend-es6/**");
	}
}