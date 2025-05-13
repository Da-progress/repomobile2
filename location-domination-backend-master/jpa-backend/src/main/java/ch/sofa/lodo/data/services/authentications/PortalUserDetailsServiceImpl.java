package ch.sofa.lodo.data.services.authentications;

import ch.sofa.lodo.data.configurations.security.SpringSecurityUser;
import ch.sofa.lodo.data.constants.UserType;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userDetailsServiceImpl")
public class PortalUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameAndUserType(username, UserType.PORTAL_USER);
		if (user == null) {
			throw new UsernameNotFoundException("No user data");
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		// for (Role role : user.getRoles()) {
		// grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleType()
		// .getType()));
		// }

		return new SpringSecurityUser(user.getUsername(), user.getPassword(), user.isAccountNonExpired(), true, true, true, grantedAuthorities);
	}
}