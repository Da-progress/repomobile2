package ch.sofa.lodo.data.services.authentications;

import ch.sofa.lodo.data.models.User;

import java.util.List;

public interface PortalUserService {

	User create(User user);

	User save(User user);

	User update(User user);

	User findByUsername(String username);

	User getCurrentUser();

	List<User> findAll();

	void delete(User user);

	User findById(Long id);
}
