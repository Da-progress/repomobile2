package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.constants.UserType;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.UserRepository;
import ch.sofa.lodo.data.services.generators.RegistrationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User persist(User entity) {
		return repository.save(entity);
	}

	@Override
	public User update(User entity) {
		return repository.save(entity);
	}

	@Override
	public User updateLimited(User entity) {
		User newP = findById(entity.getId());
		// newP.setUserType(UserType.PLAYER);
		// newP.setUsername(entity.getUserFullName());
		// newP.setPasswordHash(bCryptPasswordEncoder.encode(entity.getPassword()));
		// newP.setAddress(entity.getAddress());
		// newP.setRegisterDateTime(entity.getRegisterDateTime());
		// newP.setRecordVersion(entity.getRecordVersion());
		// newP.copyPropertiesFrom(entity);
		// newP.setPasswordHash(bCryptPasswordEncoder.encode(entity.getPassword()));
		// newP.setLastName(entity.getLastName());
		// newP.setFirstName(entity.getFirstName());
		newP.setMobileNumber(entity.getMobileNumber());

		return repository.save(newP);
	}

	@Override
	public User changePassword(User entity) {
		User newP = findById(entity.getId());
		newP.setPasswordHash(bCryptPasswordEncoder.encode(entity.getPassword()));
		newP.setCountPasswordReset(newP.getCountPasswordReset() + 1);
		return repository.save(newP);
	}

	@Override
	public User changePassword(Long id, String code, String password) {
		User newP = findById(id);
		System.out.println(".... forget reset code:" + code);
		if (code.equals(newP.getAuthorizationCode())) {
			newP.setPasswordHash(bCryptPasswordEncoder.encode(password));
			newP.setCountPasswordReset(newP.getCountPasswordReset() + 1);
			return repository.save(newP);
		}
		return null;
	}

	@Override
	public void markAsDeleted(long id) {
		User entity = findById(id);
		entity.setDeleted(true);
		repository.save(entity);
	}

	@Override
	public void delete(User entity) {
		repository.delete(entity);
	}

	@Override
	public List<User> findAll() {
		return repository.findAllByUserTypeAndDeletedFalse(UserType.PLAYER);
	}

	@Override
	public boolean authenticate(Long id, String code) {

		List<User> users = repository.findAllByIdAndAuthorizationCode(id, code);
		if (users.isEmpty()) {
			return false;
		} else if (users.size() == 1) {
			users.get(0).setAuthenticated(true);
			repository.save(users.get(0));
			return true;
		} else {
			// not allowed state
			return false;
		}
	}

	@Override
	public User findByPhoneNr(String phoneNr) {

		List<User> users = repository.findAllByMobileNumber(phoneNr);
		if (users.isEmpty()) {
			return null;
		} else if (users.size() == 1) {
			if (UserType.PLAYER.equals(users.get(0).getUserType())) {

				users.get(0).setAuthorizationCode(RegistrationCodeGenerator.generate());
				System.out.println(" .. new auth code:" + users.get(0).getAuthorizationCode());
				return repository.save(users.get(0));
			}
			return null;
		} else {
			// not allowed state
			return null;
		}
	}

	@Override
	public List<User> filterByName(String searchText, List<String> excludeUsernames, List<Long> exclude) {

		Specification<User> playerType = (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("userType"), UserType.PLAYER);
		};

		Specification<User> excludeUsername = (root, criteriaQuery, criteriaBuilder) -> {
			if (excludeUsernames == null || excludeUsernames.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("username").in(excludeUsernames).not();
			}
		};

		Specification<User> usernameLike = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.get("username"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<User> excludeById = (root, criteriaQuery, criteriaBuilder) -> {
			if (exclude == null || exclude.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("id").in(exclude).not();
			}
		};

		Specification<User> notDeletedRule = (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("deleted"), false);
		};

		return repository.findAll(playerType.and(excludeUsername).and(usernameLike).and(excludeById).and(notDeletedRule));
	}

	@Override
	public User findById(Long id) {
		Optional<User> user = repository.findById(id);

		// security check by type
		if (user.isPresent()) {
			if (UserType.PLAYER.equals(user.get().getUserType())) {
				return user.get();
			}
		}
		return null;
	}

	@Override
	public long count() {
		return repository.count();
	}
}
