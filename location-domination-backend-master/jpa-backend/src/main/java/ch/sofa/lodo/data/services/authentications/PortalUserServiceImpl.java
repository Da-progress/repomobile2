package ch.sofa.lodo.data.services.authentications;

import ch.sofa.lodo.data.configurations.security.SpringSecurityUser;
import ch.sofa.lodo.data.constants.UserType;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortalUserServiceImpl implements PortalUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User create(User user) {
		user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User save(User user) {

		if (user.getId() == null) {
			return userRepository.save(user);
		}

		return update(user);
	}

	@Override
	@Nullable
	public User findByUsername(String username) {
		return userRepository.findByUsernameAndUserType(username, UserType.PORTAL_USER);
	}

	@Override
	@Transactional
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();

		String currentUsername = ((SpringSecurityUser) principal).getUsername();
		return findByUsername(currentUsername);
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return userRepository.findAllByUserType(UserType.PORTAL_USER, Sort.by(Direction.ASC, "username"));
	}

	@Override
	public void delete(User user) {
		if (user == null) {
			return;
		}
		userRepository.delete(user);
	}

	@Override
	public User findById(Long id) {

		Optional<User> user = userRepository.findById(id);

		// security check by type
		if (user.isPresent()) {
			if (UserType.PORTAL_USER.equals(user.get()
					.getUserType())) {
				return user.get();
			}
		}
		return null;
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}
}
