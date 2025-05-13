package ch.sofa.lodo.data.configurations.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@SuppressWarnings("serial")
public class SpringSecurityUser extends User {

	public SpringSecurityUser(String username, String password, boolean enabled, boolean accountNonExpired,
							  boolean credentialsNonExpired, boolean accountNonLocked,
							  Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
}