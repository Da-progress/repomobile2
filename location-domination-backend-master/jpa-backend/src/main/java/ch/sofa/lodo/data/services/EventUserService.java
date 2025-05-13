package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventUser;
import ch.sofa.lodo.data.models.User;

import java.util.List;

public interface EventUserService extends SuperService<EventUser> {

	EventUser registerPlayer(Long eventId, Long playerId, String registrationCode);

	EventUser registerPlayer(Event event, User player, String registrationCode);

	boolean unregisterPlayer(Long event, Long player);

	boolean unregisterPlayer(Event event, User player);

	boolean isPlayerRegistered(Event event, User player);

	boolean isPlayerRegistered(Long eventId, Long playerId);

	List<User> findAllByEventId(Long id, String searchText, List<Long> exclude);
}
