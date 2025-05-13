package ch.sofa.lodo.admin.configurations;

import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class SecurityUtils {

	private SecurityUtils() {
		// Util methods only
	}

	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null && Stream.of(RequestType.values())
				.anyMatch(r -> r.getIdentifier()
						.equals(parameterValue));
	}

	public static boolean isUserLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

	public static boolean hasRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		return authentication != null && authentication.getAuthorities()
				.contains(new SimpleGrantedAuthority(role));
	}
}
