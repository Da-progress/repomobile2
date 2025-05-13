package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.User;

import java.util.List;

public interface PlayerService extends SuperService<User> {

	List<User> filterByName(String searchText, List<String> excludeUsernames, List<Long> exclude);

	public User updateLimited(User entity);

	public User changePassword(User entity);

	public boolean authenticate(Long id, String code);

	public User findByPhoneNr(String phoneNr);

	public User changePassword(Long id, String code, String password);

	void markAsDeleted(long id);

	long count();
}
